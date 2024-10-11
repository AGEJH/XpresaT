from flask import Flask, request, jsonify # type: ignore  #TRUNCATE para eliminar en sql registros
from models import db, User, UserResponse  # Importar db y modelos
#from security import check_password, hash_password
from flask_cors import CORS # type: ignore # Manejar las solicitudes desde tu aplicación Android.
from flask_migrate import Migrate  # type: ignore # Importar Flask-Migrate aquí, Flask-Migrate, es excelente para manejar las migraciones de la base de datos sin problemas.
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
db.init_app(app)  # Inicializar SQLAlchemy con la app de Flask
migrate = Migrate(app, db)  # Inicializar Flask-Migrate aquí
CORS(app)  # Aplicar CORS a la app


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
    
    
    
 # En el registro de usuario
# Endpoint de registro
@app.route('/register', methods=['POST'])
def register_user():
    """Registra a un nuevo usuario."""
    data = request.get_json()
    
    # Extraer datos del JSON
    email = data.get('email')
    password = data.get('password')  # Contraseña en texto plano
    name = data.get('name')
    lastname = data.get('lastname')

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
    new_user = User(email=email, password=password, name=name, lastname=lastname)  # Aquí guardamos directamente la contraseña en texto plano

    try:
        db.session.add(new_user)
        db.session.commit()
        app.logger.debug(f'Usuario registrado: {new_user.email}, Contraseña: {new_user.password}')
    except Exception as e:
        app.logger.error(f'Error al registrar usuario: {e}')
        db.session.rollback()
        return jsonify({"error": "Error al registrar usuario"}), 500

    return jsonify({"message": "Usuario registrado exitosamente"}), 201



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