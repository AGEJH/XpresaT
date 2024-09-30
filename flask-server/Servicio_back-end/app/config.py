import os
# la clave secreta y la URI de la base de datos deberían almacenarse en variables de entorno,
# como ya lo está haciendo, para proteger datos sensibles.
class Config:
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'una_llave_secreta_muy_segura'
    SQLALCHEMY_DATABASE_URI = os.environ.get('DATABASE_URL') or 'mysql://root:1234@127.0.0.1/xpresat?charset=utf8mb4'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
