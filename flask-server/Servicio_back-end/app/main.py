from flask import Flask, request, jsonify, session  # type: ignore          #TRUNCATE sirve para eliminar en sql registros
from datetime import datetime
from models import Post, db, User, UserResponse, Friend  # Asegúrate de importar la instancia de tu base de datos
from flask_migrate import Migrate      # type: ignore # Importar Flask-Migrate aquí, Flask-Migrate, es excelente para manejar las migraciones de la base de datos sin problemas.
#from security import check_password, hash_password
from flask_cors import CORS # type: ignore # Manejar las solicitudes desde tu aplicación Android.
from config import Config  # Importa_la_configuración aquí
from nltk.tokenize import word_tokenize # type: ignore
import nltk  # type: ignore 
import traceback
nltk.download('punkt')

diccionario_emociones = {
    'tanta': 0.02, 'tristeza': 0.99, 'días': 0.05, 'felices': 0.1,
    'no': 0.01, 'siento': 0.03, 'nada': 0.5
}

app = Flask(__name__)  
app.config.from_object(Config)  # Cargar configuración
migrate = Migrate(app, db)  # Inicializar Flask-Migrate aquí

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

        # Serializar los datos de las solicitudes
        solicitudes_data = [{
            "sender_id": solicitud.sender_id,
            "receptor_id": solicitud.receptor_id,
            "is_accepted": solicitud.is_accepted,
            "is_readed": solicitud.is_readed,
            "created_at": solicitud.created_at
        } for solicitud in solicitudes]

        return jsonify(solicitudes_data), 200
    except Exception as e:
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

    if friend_request:
        if friend_request.is_accepted:
            # Son amigos
            return jsonify({"is_friend": True, "is_request_sent": False})
        else:
            # Hay una solicitud pendiente
            return jsonify({"is_friend": False, "is_request_sent": True})
    else:
        # No son amigos ni hay solicitud pendiente
        return jsonify({"is_friend": False, "is_request_sent": False})
    
    
    

# Endpoint Aceptar solicitud de amistad
@app.route('/aceptar_solicitud', methods=['POST'])
def aceptar_solicitud():
    data = request.get_json()
    usuario_id = data.get('usuario_id')
    amigo_id = data.get('amigo_id')

    if not usuario_id or not amigo_id:
        return jsonify({"error": "Datos faltantes"}), 400

    # Buscar la solicitud de amistad
    solicitud = Friend.query.filter_by(sender_id=amigo_id, receptor_id=usuario_id, is_accepted=False).first()

    if not solicitud:
        return jsonify({"error": "Solicitud de amistad no encontrada"}), 404

    # Actualizar el estado a aceptado
    solicitud.is_accepted = True
    db.session.commit()

    return jsonify({"message": "Solicitud de amistad aceptada"}), 200



@app.route('/get_posts', methods=['GET'])
def get_posts():
    username = request.args.get('username')
    if not username:
        return jsonify({"error": "Falta el parámetro 'username'"}), 400

    try:
        # Obtener el usuario por nombre de usuario
        usuario = db.session.query(User).filter_by(username=username).first()
        if not usuario:
            return jsonify({"error": "Usuario no encontrado"}), 404

        # Obtener publicaciones del usuario
        posts = db.session.query(Post).filter_by(author=username).all()
        
        # Serializar los datos de las publicaciones
        posts_data = [{
            "content": post.content,
            "author": post.author,
            "created_at": post.created_at
        } for post in posts]
        
        return jsonify(posts_data), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500




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