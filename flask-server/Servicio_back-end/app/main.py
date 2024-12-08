from flask import Flask, request, jsonify, session  # type: ignore          #TRUNCATE sirve para eliminar en sql registros
from datetime import datetime
from db_utils import get_db_connection
from flask_mail import Mail, Message
from models import Comment, Like, Post, db, User, UserResponse, Friend, Family  # Asegúrate de importar la instancia de tu base de datos
from flask_migrate import Migrate      # type: ignore # Importar Flask-Migrate aquí, Flask-Migrate, es excelente para manejar las migraciones de la base de datos sin problemas.
#from security import check_password, hash_password
from flask_cors import CORS # type: ignore # Manejar las solicitudes desde tu aplicación Android.
from config import Config  # Importa_la_configuración aquí
from nltk.tokenize import word_tokenize # type: ignore
import nltk  # type: ignore 
import traceback
import logging

logging.basicConfig(level=logging.INFO)  # Configuración básica, se puede ajustar el nivel

nltk.download('punkt')

diccionario_emociones = {
    'tanta': 0.02, 'tristeza': 0.99, 'días': 0.05, 'felices': 0.1,
    'no': 0.01, 'siento': 0.03, 'nada': 0.5
}

app = Flask(__name__)  
app.config.from_object(Config)  # Cargar configuración
migrate = Migrate(app, db)  # Inicializar Flask-Migrate aquí

# Configuración de Flask-Mail (usa tu servidor SMTP)
app.config['MAIL_SERVER'] = 'smtp.gmail.com'  # Servidor SMTP
app.config['MAIL_PORT'] = 587  # Puerto SMTP (587 para TLS)
app.config['MAIL_USE_TLS'] = True  # Usa TLS (seguridad)
app.config['MAIL_USERNAME'] = 'tu_correo@gmail.com'  # Tu correo
app.config['MAIL_PASSWORD'] = 'tu_contraseña_de_aplicación'  # Contraseña de aplicación (NO la contraseña de tu cuenta)
app.config['MAIL_DEFAULT_SENDER'] = 'tu_correo@gmail.com'  # Correo que se usará como remitente

# Inicializa Flask-Mail
mail = Mail(app)

db.init_app(app)  # Inicializar SQLAlchemy con la app de Flask
CORS(app, methods=["POST"]) # Aplicar CORS a la app


@app.route('/')
def home():
    return "Bienvenido a la página principal de la API!"


# Endpoint de login
@app.route('/login', methods=['POST'])
def login_user():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    user = User.query.filter_by(email=email).first()

    if not user:
        return jsonify({"success": False, "message": "Usuario no encontrado"}), 404

    if user.password == password:  # Asegúrate de que la comparación de contraseñas sea segura
        return jsonify({"success": True, "message": "Inicio de sesión exitoso", "username": user.email}), 200
    else:
        return jsonify({"success": False, "message": "Contraseña incorrecta"}), 401
    
    
    
#Endpoint de registro
@app.route('/register', methods=['POST'])
def register_user():
    # Imprimir el cuerpo de la solicitud
    print("Solicitud recibida:", request.json)
    
    """Registra a un nuevo usuario."""
    data = request.get_json()

    # Extraer datos del JSON con los nombres correctos que llegan desde el cliente
    email = data.get('email')
    password = data.get('password')  # Contraseña en texto plano
    name = data.get('nombre')        # Cambiado a 'nombre'
    lastname = data.get('apellido')  # Cambiado a 'apellido'

    # Verificar si el usuario ya existe
    existing_user = User.query.filter_by(email=email).first()
    if existing_user:
        app.logger.debug(f'El email {email} ya está registrado.')
        return jsonify({"error": "Usuario ya existe"}), 409

    # Validación de campos
    if not all([email, password, name, lastname]):
        app.logger.error('Faltan campos requeridos en la solicitud de registro.')
        return jsonify({"error": "Campos requeridos faltantes"}), 400

    # Guardar la contraseña en texto plano (sin hash)
    app.logger.debug(f'Contraseña en texto plano que se intenta guardar: {password}')

    # Crear el nuevo usuario
    new_user = User(email=email, password=password, name=name, lastname=lastname)

    try:
        db.session.add(new_user)
        db.session.commit()
        app.logger.debug(f'Usuario registrado: {new_user.email}, Contraseña: {new_user.password}')
    except Exception as e:
        app.logger.error(f'Error al registrar usuario: {e}')
        db.session.rollback()
        return jsonify({"error": "Error al registrar usuario"}), 500

    return jsonify({"message": "Usuario registrado exitosamente"}), 201
    
    
    
# Endpoint Buscar usuarios por nombre o email
@app.route('/buscar_usuarios', methods=['GET'])
def buscar_usuarios():
    query = request.args.get('query')

    if not query:
        return jsonify({"error": "No se proporciono un termino de búsqueda"}), 400

    usuarios = User.query.filter((User.name.ilike(f"%{query}%")) | (User.email.ilike(f"%{query}%"))).all()     # Buscar usuarios que coincidan con el nombre o el email


    if not usuarios:
        return jsonify({"message": "No se encontraron usuarios"}), 200

  
    usuarios_lista = [{"id": user.id, "nombre": user.name, "email": user.email} for user in usuarios]   # Serializar los usuarios encontrados (convertir de formato JSON a bytes)

    return jsonify({"usuarios": usuarios_lista}), 200

    
    
# Endpoint Enviar solicitud de amistad
@app.route('/enviar_solicitud', methods=['POST']) 
def enviar_solicitud():
    data = request.get_json()
    email_usuario = data.get("email_usuario")
    email_amigo = data.get("email_amigo")

    # Debugging logs
    print(f"Email Usuario: {email_usuario}, Email Amigo: {email_amigo}")

    # Obtén los usuarios por su correo
    sender = User.query.filter_by(email=email_usuario).first()
    receptor = User.query.filter_by(email=email_amigo).first()

    # Verifica si ambos usuarios existen
    if not sender or not receptor:
        print("Usuario o amigo no encontrado")
        return jsonify({"error": "Usuario o amigo no encontrado"}), 404

    # Verifica si la solicitud ya existe y no ha sido aceptada
    solicitud_existente = Friend.query.filter_by(
        sender_id=sender.id, receptor_id=receptor.id, is_accepted=False
    ).first()

    if solicitud_existente:
        return jsonify({"error": "Solicitud ya enviada"}), 400

    # Inserta la solicitud de amistad si no existe
    try:
        nueva_solicitud = Friend(sender_id=sender.id, receptor_id=receptor.id, is_accepted=False, is_readed=False)
        db.session.add(nueva_solicitud)
        db.session.commit()
        return jsonify({"message": "Solicitud de amistad enviada exitosamente"}), 200
    except Exception as e:
        db.session.rollback()
        return jsonify({"error": str(e)}), 500
    
    
    
@app.route('/obtener_solicitudes', methods=['GET'])
def obtener_solicitudes():
    email_usuario = request.args.get('email_usuario')
    if not email_usuario:
        return jsonify({"error": "Falta el parámetro 'email_usuario'"}), 400

    try:
        # Obtener el usuario receptor basado en el email
        usuario_receptor = db.session.query(User).filter_by(email=email_usuario).first()
        if not usuario_receptor:
            return jsonify({"error": "Usuario no encontrado"}), 404

        # Obtener solicitudes pendientes dirigidas a este usuario
        solicitudes = db.session.query(Friend).filter_by(
            receptor_id=usuario_receptor.id,
            is_accepted=False
        ).all()

        # Serializar los datos de las solicitudes incluyendo nombre y apellido del remitente
        solicitudes_data = [{
            "sender_id": solicitud.sender_id,
            "receptor_id": solicitud.receptor_id,
            "is_accepted": solicitud.is_accepted,
            "is_readed": solicitud.is_readed,
            "created_at": solicitud.created_at,
            "email_amigo": solicitud.sender.email,          # Agregar el email del remitente
            "nombre_amigo": solicitud.sender.name,          # Agregar el nombre del remitente
            "apellido_amigo": solicitud.sender.lastname     # Agregar el apellido del remitente
        } for solicitud in solicitudes]

        return jsonify(solicitudes_data), 200
    except Exception as e:
        app.logger.error(f"Error al obtener solicitudes: {str(e)}", exc_info=True)
        return jsonify({"error": str(e)}), 500

    
    

@app.route('/friend_status', methods=['GET'])
def friend_status():
    # Obtiene los parámetros de la URL
    email_usuario = request.args.get('email_usuario')
    email_amigo = request.args.get('email_amigo')

    # Verificación de que ambos correos están presentes
    if not email_usuario or not email_amigo:
        return jsonify({"error": "Ambos emails deben ser proporcionados"}), 400

    # Busca los usuarios por email en la base de datos
    usuario = User.query.filter_by(email=email_usuario).first()
    amigo = User.query.filter_by(email=email_amigo).first()

    if not usuario or not amigo:
        return jsonify({"error": "Usuario o amigo no encontrado"}), 404

    # Busca la relación de amistad en la tabla Friend
    friend_request = Friend.query.filter(
        ((Friend.sender_id == usuario.id) & (Friend.receptor_id == amigo.id)) |
        ((Friend.sender_id == amigo.id) & (Friend.receptor_id == usuario.id))
    ).first()

    # Construir la respuesta con nombre y apellido del amigo
    response_data = {
        "is_friend": False,
        "is_request_sent": False,
        "nombreAmigo": amigo.name,
        "apellidoAmigo": amigo.lastname
    }

    if friend_request:
        if friend_request.is_accepted:
            response_data["is_friend"] = True
        else:
            response_data["is_request_sent"] = True

    return jsonify(response_data)

    


# Endpoint Aceptar solicitud de amistad
@app.route('/aceptar_solicitud', methods=['POST'])
def aceptar_solicitud():
    data = request.get_json()
    usuario_id = data.get('usuario_id')
    amigo_id = data.get('amigo_id')

    # Validar los datos recibidos
    if not usuario_id or not amigo_id:
        return jsonify({"error": "Datos faltantes"}), 400

    try:
        # Verificar si ambos usuarios existen
        usuario = User.query.get(usuario_id)
        amigo = User.query.get(amigo_id)
        if not usuario or not amigo:
            return jsonify({"error": "Usuario o amigo no encontrado"}), 404

        # Buscar la solicitud de amistad pendiente
        solicitud = Friend.query.filter_by(sender_id=amigo_id, receptor_id=usuario_id, is_accepted=False).first()
        
        if not solicitud:
            return jsonify({"error": "Solicitud de amistad no encontrada"}), 404

        # Actualizar el estado a aceptado
        solicitud.is_accepted = True
        db.session.commit()

        # (Opcional) Crear una relación bidireccional
        # nueva_relacion = Friend(sender_id=usuario_id, receptor_id=amigo_id, is_accepted=True)
        # db.session.add(nueva_relacion)
        # db.session.commit()

        return jsonify({"message": "Solicitud de amistad aceptada", "usuario": usuario.name, "amigo": amigo.name}), 200

    except Exception as e:
        db.session.rollback()  # Revertir cambios en caso de error
        return jsonify({"error": "Ocurrió un error al procesar la solicitud", "details": str(e)}), 500



@app.route('/reject_friend_request', methods=['POST'])
def reject_friend_request():
    data = request.json
    email_usuario = data.get('email_usuario')
    email_amigo = data.get('email_amigo')
    
    # Obtén los IDs de los usuarios a partir de sus correos electrónicos
    usuario = User.query.filter_by(email=email_usuario).first()
    amigo = User.query.filter_by(email=email_amigo).first()
    
    if not usuario or not amigo:
        return jsonify({"error": "Usuario o amigo no encontrado"}), 404
    
    # Busca la solicitud de amistad en la tabla friend
    solicitud = Friend.query.filter_by(sender_id=amigo.id, receptor_id=usuario.id, is_accepted=0).first()
    
    if solicitud:
        # Opción 1: Eliminar la solicitud de amistad
        db.session.delete(solicitud)
        db.session.commit()
        return jsonify({"message": "Solicitud de amistad rechazada"}), 200
    else:
        return jsonify({"error": "Solicitud de amistad no encontrada"}), 404


#a Post para Crear un comentario
@app.route('/posts/<int:post_id>/comments', methods=['POST'])
def create_comment(post_id):
    data = request.json
    new_comment = Comment(
        type_id=1,  # Tipo 1 para post
        ref_id=post_id,
        user_id=data['user_id'],
        comment_id=data.get('parent_comment_id'),  # Puede ser None si es un comentario principal
        content=data['content']
    )
    db.session.add(new_comment)
    db.session.commit()
    return jsonify({'message': 'Comentario creado', 'comment_id': new_comment.id}), 201



# Recupera todos los comentarios de un post, organizándolos en una estructura jerárquica.
@app.route('/posts/<int:post_id>/comments', methods=['GET'])
def get_comments(post_id):
    comments = Comment.query.filter_by(type_id=1, ref_id=post_id).all()
    comments_list = [
        {
            'id': comment.id,
            'content': comment.content,
            'user_id': comment.user_id,
            'parent_comment_id': comment.comment_id,
            'created_at': comment.created_at
        }
        for comment in comments
    ]
    return jsonify(comments_list), 200


@app.route('/comments/<int:comment_id>', methods=['PUT'])
def update_comment(comment_id):
    data = request.json
    comment = Comment.query.get(comment_id)
    
    if not comment:
        return jsonify({'error': 'Comentario no encontrado'}), 404

    # Verificar que el usuario que edita es el dueño del comentario
    if comment.user_id != data['user_id']:
        return jsonify({'error': 'No autorizado'}), 403

    # Actualizar contenido del comentario
    comment.content = data.get('content', comment.content)
    db.session.commit()

    return jsonify({'message': 'Comentario actualizado'}), 200



@app.route('/comments/<int:comment_id>', methods=['DELETE'])
def delete_comment(comment_id):
    data = request.json
    comment = Comment.query.get(comment_id)

    if not comment:
        return jsonify({'error': 'Comentario no encontrado'}), 404

    # Verificar que el usuario que elimina es el dueño del comentario
    if comment.user_id != data['user_id']:
        return jsonify({'error': 'No autorizado'}), 403

    db.session.delete(comment)
    db.session.commit()

    return jsonify({'message': 'Comentario eliminado'}), 200




#permite dar o quitar un like, dependiendo de si el like ya existe.
@app.route('/<int:type_id>/<int:ref_id>/like', methods=['POST'])
def toggle_like(type_id, ref_id):
    data = request.json
    user_id = data['user_id']
    
    existing_like = Like.query.filter_by(type_id=type_id, ref_id=ref_id, user_id=user_id).first()
    if existing_like:
        db.session.delete(existing_like)
        db.session.commit()
        return jsonify({'message': 'Like removido'}), 200
    else:
        new_like = Like(type_id=type_id, ref_id=ref_id, user_id=user_id)
        db.session.add(new_like)
        db.session.commit()
        return jsonify({'message': 'Like agregado'}), 201
    
    
@app.route('/family_request', methods=['POST'])
def send_family_request():
    data = request.json
    user_id = data.get('user_id')
    family_member_id = data.get('family_member_id')
    relation = data.get('relation')

    if not user_id or not family_member_id:
        return jsonify({"error": "Datos incompletos"}), 400

    # Verificar que no exista ya una relación
    existing_relation = Family.query.filter_by(user_id=user_id, family_member_id=family_member_id).first()
    if existing_relation:
        return jsonify({"error": "Ya existe una relación"}), 400

    # Crear una nueva solicitud
    family_request = Family(user_id=user_id, family_member_id=family_member_id, relation=relation, is_confirmed=False)
    db.session.add(family_request)
    db.session.commit()

    return jsonify({"message": "Solicitud enviada con éxito"}), 201




@app.route('/family_request/<int:request_id>', methods=['PUT'])
def respond_family_request(request_id):
    data = request.json
    is_confirmed = data.get('is_confirmed')  # True para aceptar, False para rechazar

    family_request = Family.query.get(request_id)
    if not family_request:
        return jsonify({"error": "Solicitud no encontrada"}), 404

    if is_confirmed:
        family_request.is_confirmed = True
        db.session.commit()
        return jsonify({"message": "Solicitud aceptada"}), 200
    else:
        db.session.delete(family_request)
        db.session.commit()
        return jsonify({"message": "Solicitud rechazada"}), 200
    
    
    
@app.route('/family/<int:user_id>', methods=['GET'])
def get_family_members(user_id):
    family_members = Family.query.filter_by(user_id=user_id, is_confirmed=True).all()
    result = [
        {
            "id": member.id,
            "family_member_id": member.family_member_id,
            "relation": member.relation,
            "created_at": member.created_at
        } for member in family_members
    ]
    return jsonify(result), 200


@app.route('/add_trusted_contact', methods=['POST'])
def add_trusted_contact():
    data = request.json
    user_id = data.get('user_id')
    relative_id = data.get('relative_id')

    if not user_id or not relative_id:
        return jsonify({"error": "Faltan parámetros 'user_id' o 'relative_id'"}), 400

    try:
        connection = get_db_connection()
        with connection.cursor() as cursor:
            query = """
                INSERT INTO trusted_contacts (user_id, relative_id, created_at) 
                VALUES (%s, %s, NOW())
            """
            cursor.execute(query, (user_id, relative_id))
            connection.commit()
        
        return jsonify({"message": "Contacto de confianza añadido con éxito"}), 201
    except Exception as e:
        print("Error al añadir contacto de confianza:", e)
        return jsonify({"error": "Error interno del servidor"}), 500
    
    

@app.route('/enviar-correo', methods=['POST'])
def enviar_correo():
    """Envía un correo a una dirección de correo dada."""
    data = request.json  # Obtén los datos del cuerpo de la solicitud
    try:
        mensaje = Message(
            subject=data.get('asunto', 'Sin Asunto'),  # Asunto del correo
            recipients=[data['email']],  # Lista de destinatarios
            body=data['mensaje']  # Cuerpo del mensaje
        )
        mail.send(mensaje)
        return jsonify({'mensaje': 'Correo enviado exitosamente'}), 200
    except Exception as e:
        return jsonify({'error': f'Error al enviar el correo: {str(e)}'}), 500



@app.route('/get_posts', methods=['GET'])
def get_posts():
    name = request.args.get('name')

    # Verificar si el parámetro 'username' está presente
    if not name:
        logging.warning("Solicitud sin 'name' proporcionado")
        return jsonify({"error": "username is required"}), 400

    try:
        # Obtener el usuario por nombre de usuario
        usuario = db.session.query(User).filter_by(name=name).first()
        if not usuario:
            logging.info(f"Usuario '{name}' no encontrado")
            return jsonify({"error": "Usuario no encontrado"}), 404

        # Obtener publicaciones del usuario
        posts = db.session.query(Post).filter_by(author=name).all()
        
        # Serializar los datos de las publicaciones
        posts_data = [{
            "content": post.content,
            "author": post.author,
            "created_at": post.created_at
        } for post in posts]

        logging.info(f"Posts obtenidos exitosamente para el usuario '{name}': {len(posts)} publicaciones")
        return jsonify(posts_data), 200

    except Exception as e:
        # Registrar la excepción detalladamente
        logging.error(f"Error al obtener posts para el usuario '{name}': {str(e)}", exc_info=True)
        return jsonify({"error": "Ocurrió un error al obtener las publicaciones"}), 500




@app.route('/analyze', methods=['POST'])
def analyze_text():
    data = request.get_json()
    if 'email' not in data or 'respuestas' not in data:
        return jsonify({"error": "Falta el campo 'email' o 'respuestas'"}), 400
    email = data['email']
    responses = data['respuestas']
    results = []
    for text in responses:
        words = word_tokenize(text.lower(), language='spanish')
        sentiment_score = sum(diccionario_emociones.get(word, 0) for word in words)
        results.append({'text': text, 'sentiment_score': sentiment_score})
        new_response = UserResponse(email=email, response=text, sentiment_score=sentiment_score)
        db.session.add(new_response)
    db.session.commit()
    return jsonify(results)


@app.route('/test', methods=['GET'])
def test():
    return jsonify({"message": "El API funciona correctamente!"})


@app.route('/dbtest')
def db_test():
    try:
        user_count = User.query.count()
        return jsonify({"message": f"Hay {user_count} usuarios en la base de datos."}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/get_user', methods=['GET'])
def get_user():
    email = request.args.get('email')

    if not email:
        return jsonify({"error": "No se proporciona el email"}), 400

    user = User.query.filter_by(email=email).first()

    if not user:
        return jsonify({"error": "Usuario no encontrado"}), 404

    return jsonify({
        "message": "Usuario encontrado con éxito",
        "success": True,
        "username": user.name  # Cambié 'user.username' por 'user.name'
    }), 200

if __name__ == '__main__':
    app.run(debug=True)