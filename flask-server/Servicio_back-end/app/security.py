import bcrypt
from flask import current_app
from passlib.hash import scrypt

# Función para generar el hash de la contraseña usando bcrypt
def hash_password(password):
    from models import db, User 
    salt = bcrypt.gensalt()  # Genera un salt único para cada contraseña
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)  # Crea el hash
    return hashed.decode('utf-8')  # Devuelve el hash como un string

# Función para verificar si el hash es de bcrypt
def is_bcrypt_hash(hashed_password):
    if isinstance(hashed_password, bytes):
        hashed_password = hashed_password.decode('utf-8')
    return hashed_password.startswith("$2b$")

# Función para verificar la contraseña
def check_password(password, hashed_password):
    try:
        # Verifica si el hash está en formato correcto
        if isinstance(hashed_password, bytes):
            hashed_password = hashed_password.decode('utf-8')

        # Verifica la contraseña
        result = bcrypt.checkpw(password.encode('utf-8'), hashed_password.encode('utf-8'))

        if result:
            return True
        else:
            current_app.logger.debug('La comparación de la contraseña falló.')
            return False
    except Exception as e:
        current_app.logger.error(f'Error al verificar la contraseña: {e}')
        return False
    
    
# Función para regenerar contraseñas antiguas
#def regenerate_passwords():
 #   from models import db, User
  #  users = User.query.all()
   # for user in users:
    #    if isinstance(user.password, bytes):
     #       user.password = user.password.decode('utf-8')

      #  if not is_bcrypt_hash(user.password):
       #     user.password = hash_password(user.password)
    #db.session.commit()
