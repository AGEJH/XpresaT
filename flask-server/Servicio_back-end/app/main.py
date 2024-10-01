from flask import Flask, request, jsonify # type: ignore  #TRUNCATE para eliminar en sql registros
from models import db, User, UserResponse  # Importar db y modelos
from security import check_password, hash_password, bcrypt
from flask_cors import CORS # type: ignore # Manejar las solicitudes desde tu aplicación Android.
from flask_migrate import Migrate  # type: ignore # Importar Flask-Migrate aquí, Flask-Migrate, es excelente para manejar las migraciones de la base de datos sin problemas.
from config import Config  # Importa la configuración aquí
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
    if user is None:
        app.logger.debug(f'Usuario no encontrado: {email}')
        return jsonify({"error": "Usuario no encontrado"}), 404

    # Verifica la contraseña
    if check_password(password, user.password):
        app.logger.debug(f'Inicio de sesión exitoso para: {email}')
        return jsonify({"message": "Inicio de sesión exitoso"}), 200
    else:
        app.logger.debug(f'Contraseña incorrecta para el usuario: {email}')
        return jsonify({"error": "Contraseña incorrecta"}), 401



@app.route('/register', methods=['POST'])
def register_user():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')
    name = data.get('name')
    lastname = data.get('lastname')

    existing_user = User.query.filter_by(email=email).first()
    if existing_user:
        app.logger.debug(f'El email {email} ya está registrado.')
        return jsonify({"error": "Usuario ya existe"}), 409

    if not all([email, password, name, lastname]):
        app.logger.error('Faltan campos requeridos en la solicitud de registro.')
        return jsonify({"error": "Campos requeridos faltantes"}), 400

    # Generación del hash de la contraseña
    hashed_password = hash_password(password)  # Genera el hash una vez
    app.logger.debug(f'Hash que se intentará guardar: {hashed_password}')
    
    # Creación del nuevo usuario
    new_user = User(email=email, password=hashed_password, name=name, lastname=lastname)

    try:
        db.session.add(new_user)
        db.session.commit()
        app.logger.debug(f'Usuario registrado: {new_user.email}, Hash: {new_user.password}')
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