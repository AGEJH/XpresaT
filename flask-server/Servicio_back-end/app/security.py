import hashlib
from flask import current_app # type: ignore

# Función para generar el hash de la contraseña usando SHA-256
def hash_password(password):
    """Hashea la contraseña original usando SHA-256."""
    current_app.logger.debug(f'Contraseña original: {password}')  # Contraseña original
    hashed = hashlib.sha256(password.encode('utf-8')).hexdigest()  # Crea el hash con SHA-256
    current_app.logger.debug(f'Hash generado con SHA-256: {hashed}')  # Hash generado
    return hashed  # Devuelve el hash generado

# Función para verificar la contraseña
def check_password(password, hashed_password):
    """Compara la contraseña original (en texto plano) con el hash almacenado."""
    current_app.logger.debug(f'Contraseña a verificar: {password}')  # Contraseña ingresada en texto plano
    current_app.logger.debug(f'Hash almacenado: {hashed_password}')  # Hash almacenado en la base de datos

    # Genera el hash de la contraseña ingresada en texto plano
    hashed_input = hashlib.sha256(password.encode('utf-8')).hexdigest()

    current_app.logger.debug(f'Hash generado para la contraseña ingresada: {hashed_input}')  # Muestra el hash generado

    # Compara el hash generado con el hash almacenado
    if hashed_input == hashed_password:
        current_app.logger.debug('La comparación de la contraseña fue exitosa.')
        return True
    else:
        current_app.logger.debug('La comparación de la contraseña falló.')
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
