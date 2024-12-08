from models import db  # Importa la instancia de la base de datos

def get_db_connection():
    """Obtiene una conexión de base de datos utilizando SQLAlchemy."""
    try:
        connection = db.session.connection()
        return connection
    except Exception as e:
        print(f'Error al obtener la conexión de la base de datos: {str(e)}')
        return None
