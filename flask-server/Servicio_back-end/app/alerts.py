from flask import Blueprint, jsonify, request
from datetime import datetime, timedelta
from sqlalchemy import func
from db_utils import db_session  # Asumimos que tienes un archivo de utils para la base de datos
from models import Post, Alert, UserRelation  # Importar tus modelos

alert_bp = Blueprint('alert', __name__)

@alert_bp.route('/check_alerts', methods=['POST'])
def check_alerts():
    """Verifica las alertas para todos los usuarios y activa la lógica de visibilidad del icono."""
    try:
        # Obtener la fecha de hoy
        now = datetime.now()

        # Consultar todas las publicaciones de la última semana
        users = db_session.query(Post.user_id).distinct().all()

        for user in users:
            user_id = user.user_id

            # Publicaciones de la última semana
            one_week_ago = now - timedelta(days=7)
            posts_week = db_session.query(Post).filter(
                Post.user_id == user_id,
                Post.created_at >= one_week_ago,
                Post.sentiment == 'negativo'  # Solo nos importan las publicaciones negativas
            ).all()

            if len(posts_week) >= 5:  # Cambia este número según el criterio de la "alerta mínima"
                # Generar alerta mínima si no se ha generado previamente
                create_alert(user_id, 'alerta mínima', 'Se detectaron publicaciones negativas esta semana')

            # Publicaciones de las últimas 3 semanas
            three_weeks_ago = now - timedelta(weeks=3)
            posts_three_weeks = db_session.query(Post).filter(
                Post.user_id == user_id,
                Post.created_at >= three_weeks_ago,
                Post.sentiment == 'negativo'
            ).all()

            if len(posts_three_weeks) >= 15:  # Cambia este número para el criterio de "alerta media"
                create_alert(user_id, 'alerta media', 'El usuario lleva 3 semanas publicando contenido negativo')

            # Publicaciones del último mes
            one_month_ago = now - timedelta(days=30)
            posts_month = db_session.query(Post).filter(
                Post.user_id == user_id,
                Post.created_at >= one_month_ago,
                Post.sentiment == 'negativo'
            ).all()

            if len(posts_month) >= 20:  # Cambia este número para el criterio de "alerta grave"
                create_alert(user_id, 'alerta grave', 'El usuario lleva un mes publicando contenido depresivo')

        return jsonify({"message": "Alertas verificadas con éxito"}), 200

    except Exception as e:
        print(f"Error al verificar las alertas: {str(e)}")
        return jsonify({"error": "Error al verificar alertas"}), 500


def create_alert(user_id, tipo_alerta, razon):
    """Crea una alerta solo si no existe una alerta activa del mismo tipo."""
    try:
        # Verificar si ya existe una alerta activa del mismo tipo para el usuario
        alert_exist = db_session.query(Alert).filter(
            Alert.user_id == user_id,
            Alert.tipo == tipo_alerta,
            Alert.is_icon_shown == 0  # Asegurar que solo se muestre si no se ha activado antes
        ).first()

        if not alert_exist:
            new_alert = Alert(
                user_id=user_id,
                tipo=tipo_alerta,
                razon=razon,
                is_icon_shown=0  # Indica que no se ha mostrado en la barra de navegación
            )
            db_session.add(new_alert)
            db_session.commit()

            print(f"Alerta creada para el usuario {user_id} - {tipo_alerta}")
        else:
            print(f"El usuario {user_id} ya tiene una alerta activa de tipo {tipo_alerta}")

    except Exception as e:
        print(f"Error al crear la alerta: {str(e)}")
        
        
@alert_bp.route('/icon_status', methods=['GET'])
def icon_status():
    """Verifica si hay alguna alerta activa para mostrar el icono."""
    email_usuario = request.args.get('email')

    try:
        # Obtener el usuario por su email
        user = db_session.query(user).filter(user.email == email_usuario).first()
        
        if user:
            # Verificar si hay alertas activas para este usuario
            alert_active = db_session.query(Alert).filter(
                Alert.user_id == user.id,
                Alert.is_icon_shown == 0  # Solo mostrar alertas que no se hayan mostrado previamente
            ).first()

            if alert_active:
                return jsonify({"show_alert_icon": True})
            else:
                return jsonify({"show_alert_icon": False})

    except Exception as e:
        print(f"Error al consultar el estado del icono: {str(e)}")
        return jsonify({"error": "Error al consultar el estado del icono"}), 500

