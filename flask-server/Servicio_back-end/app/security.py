from flask import current_app # type: ignore
import bcrypt  # type: ignore

# Función para generar el hash de la contraseña usando bcrypt
def hash_password(password):
    current_app.logger.debug(f'Contraseña original: {password}')  # Log de la contraseña original
    salt = bcrypt.gensalt()  # Genera un salt único para cada contraseña
    current_app.logger.debug(f'Salt generado: {salt.decode("utf-8")}')  # Log del salt generado
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)  # Crea el hash
    current_app.logger.debug(f'Hash generado: {hashed.decode("utf-8")}')  # Log del hash generado
    return hashed.decode('utf-8')  # Devuelve el hash como un string

# Función para verificar si el hash es de bcrypt
def is_bcrypt_hash(hashed_password):
    if isinstance(hashed_password, bytes):
        hashed_password = hashed_password.decode('utf-8')
    return hashed_password.startswith("$2b$")

#Función para verificar la contraseña
def check_password(password, hashed_password):
    current_app.logger.debug(f'Contraseña a verificar: {password}')  # Log de la contraseña ingresada
    current_app.logger.debug(f'Hash almacenado: {hashed_password}')  # Log del hash que está en la base de datos
    
    try:
        # Verifica la contraseña con bcrypt
        result = bcrypt.checkpw(password.encode('utf-8'), hashed_password.encode('utf-8'))
        
        if result:
            current_app.logger.debug('La comparación de la contraseña fue exitosa.')
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
