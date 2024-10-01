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
    if not user:
        app.logger.debug(f'Usuario no encontrado: {email}')
        return jsonify({"error": "Usuario no encontrado"}), 404

    app.logger.debug(f'Intento de inicio de sesión para: {email}')
    app.logger.debug(f'Contraseña ingresada: {password}')
    app.logger.debug(f'Hash de la contraseña almacenada: {user.password}')

    if not check_password(password, user.password):
        app.logger.debug(f'Contraseña incorrecta para el usuario: {email}')
        return jsonify({"error": "Contraseña incorrecta"}), 401

    app.logger.debug(f'El usuario {email} ha iniciado sesión con éxito.')
    return jsonify({
        "message": "Inicio de sesión exitoso",
        "success": True,
        "username": user.name
    }), 200


@app.route('/register', methods=['POST'])
def register_user():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')
    name = data.get('name')
    lastname = data.get('lastname')

    # Verificar si el usuario ya existe
    if User.query.filter_by(email=email).first():
        return jsonify({"error": "El usuario ya existe"}), 400

    # Generar el hash de la contraseña
    hashed_password = hash_password(password)
    
     # **Agrega este log para depurar**
    app.logger.debug(f'Hash de la contraseña para el registro: {hashed_password}')

    # Crear un nuevo usuario
    new_user = User(
        name=name,
        lastname=lastname,
        email=email,
        password=hashed_password
    )
    app.logger.debug(f'Hash que se intentará guardar: {new_user.password}')
    db.session.add(new_user)
    db.session.commit()
    
    # Verificar si el hash cambió después de guardar
    app.logger.debug(f'Hash almacenado en la base de datos: {new_user.password}')

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