from flask_sqlalchemy import SQLAlchemy, relationship, Integer # type: ignore
from datetime import datetime
#from security import hash_password  # Importa la función hash_password de security.py


db = SQLAlchemy() 

class UserResponse(db.Model):
    __tablename__ = 'user_response'
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255), unique=True, nullable=False)
    response = db.Column(db.String(1024), nullable=False)
    sentiment_score = db.Column(db.Float, nullable=False)
    
     # Relación con los amigos
    friends_sent = db.relationship('Friend', foreign_keys='Friend.sender_id', backref='sender', lazy='dynamic')
    friends_received = db.relationship('Friend', foreign_keys='Friend.receptor_id', backref='receiver', lazy='dynamic')
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(50), nullable=False)
    lastname = db.Column(db.String(50), nullable=False)
    email = db.Column(db.String(255), unique=True, nullable=False)
    password = db.Column(db.String(60), nullable=False)

    def __init__(self, name, lastname, email, password):
        self.name = name
        self.lastname = lastname
        self.email = email
        self.password = password                  #hash_password(password) # Almacena la contraseña encriptada
        
class Recover(db.Model):
    __tablename__ = 'recover'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    code = db.Column(db.String(20))
    is_used = db.Column(db.Boolean, default=False)
    created_at = db.Column(db.DateTime)


    
class Country(db.Model):
    __tablename__ = 'country'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(50))
    preffix = db.Column(db.String(50))


class Sentimental(db.Model):
    __tablename__ = 'sentimental'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(50))
    

class Profile(db.Model):
    __tablename__ = 'profile'
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), primary_key=True)
    day_of_birth = db.Column(db.Date)
    gender = db.Column(db.String(1))
    country_id = db.Column(db.Integer, db.ForeignKey('country.id'))
    image = db.Column(db.String(255))
    image_header = db.Column(db.String(255))
    title = db.Column(db.String(255))
    bio = db.Column(db.String(255))
    likes = db.Column(db.Text)
    dislikes = db.Column(db.Text)
    address = db.Column(db.String(255))
    phone = db.Column(db.String(255))
    public_email = db.Column(db.String(255))
    level_id = db.Column(db.Integer, db.ForeignKey('level.id'))
    sentimental_id = db.Column(db.Integer, db.ForeignKey('sentimental.id'))

class Album(db.Model):
    __tablename__ = 'album'
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(200))
    content = db.Column(db.String(500))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    level_id = db.Column(db.Integer, db.ForeignKey('level.id'))
    created_at = db.Column(db.DateTime)

class Image(db.Model):
    __tablename__ = 'image'
    id = db.Column(db.Integer, primary_key=True)
    src = db.Column(db.String(255))
    title = db.Column(db.String(200))
    content = db.Column(db.String(500))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    level_id = db.Column(db.Integer, db.ForeignKey('level.id'))
    album_id = db.Column(db.Integer, db.ForeignKey('album.id'))
    created_at = db.Column(db.DateTime)

class Post(db.Model):
    __tablename__ = 'post'
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(500))
    content = db.Column(db.Text)
    lat = db.Column(db.Float)
    lng = db.Column(db.Float)
    start_at = db.Column(db.DateTime)
    finish_at = db.Column(db.DateTime)
    receptor_type_id = db.Column(db.Integer, default=1)  # 1: user, 2: group
    author_ref_id = db.Column(db.Integer)
    receptor_ref_id = db.Column(db.Integer)
    level_id = db.Column(db.Integer, db.ForeignKey('level.id'))
    post_type_id = db.Column(db.Integer, default=1)  # 1: status, 2: event
    created_at = db.Column(db.DateTime)

class PostImage(db.Model):
    __tablename__ = 'post_image'
    post_id = db.Column(db.Integer, db.ForeignKey('post.id'), primary_key=True)
    image_id = db.Column(db.Integer, db.ForeignKey('image.id'), primary_key=True)


class Heart(db.Model):
    __tablename__ = 'heart'
    id = db.Column(db.Integer, primary_key=True)
    type_id = db.Column(db.Integer, default=1)  # 1: post, 2: image
    ref_id = db.Column(db.Integer)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    created_at = db.Column(db.DateTime)

class Comment(db.Model):
    __tablename__ = 'comment'
    id = db.Column(db.Integer, primary_key=True)
    type_id = db.Column(db.Integer)
    ref_id = db.Column(db.Integer)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    comment_id = db.Column(db.Integer, db.ForeignKey('comment.id'))
    content = db.Column(db.Text)
    created_at = db.Column(db.DateTime)


class Friend(db.Model):
    __tablename__ = 'friend'
    id = db.Column(db.Integer, primary_key=True)
    sender_id = db.Column(db.Integer, db.ForeignKey('user.id'))  # Usuario que envía la solicitud
    receptor_id = db.Column(db.Integer, db.ForeignKey('user.id'))  # Usuario que recibe la solicitud
    is_accepted = db.Column(db.Boolean, default=False)  # Indica si la solicitud fue aceptada
    is_readed = db.Column(db.Boolean, default=False)  # Indica si fue leída la solicitud
    created_at = db.Column(db.DateTime, default=datetime.utcnow)  # Fecha de creación


class Conversation(db.Model):
    __tablename__ = 'conversation'
    id = db.Column(db.Integer, primary_key=True)
    sender_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    receptor_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    created_at = db.Column(db.DateTime)

class Message(db.Model):
    __tablename__ = 'message'
    id = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.Text)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    conversation_id = db.Column(db.Integer, db.ForeignKey('conversation.id'))
    created_at = db.Column(db.DateTime)
    is_readed = db.Column(db.Boolean, default=False)

class Notification(db.Model):
    __tablename__ = 'notification'
    id = db.Column(db.Integer, primary_key=True)
    not_type_id = db.Column(db.Integer)
    type_id = db.Column(db.Integer)
    ref_id = db.Column(db.Integer)
    receptor_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    sender_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    is_readed = db.Column(db.Boolean, default=False)
    created_at = db.Column(db.DateTime)

class Team(db.Model):
    __tablename__ = 'team'
    id = db.Column(db.Integer, primary_key=True)
    image = db.Column(db.String(200))
    title = db.Column(db.String(200))
    description = db.Column(db.String(500))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    status = db.Column(db.Integer, default=1)  # 1: open, 2: closed
    created_at = db.Column(db.DateTime)


    def __repr__(self):
        return f'<UserResponse {self.email}>'
    
    def __repr__(self):
        return f'<User {self.username}>'
    
     
    
    
