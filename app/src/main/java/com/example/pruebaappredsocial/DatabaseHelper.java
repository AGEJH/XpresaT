package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nombre de la base de datos y su versión
    private static final String DATABASE_NAME = "xpresate.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor de la clase
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    //A continuación  el campo id será autoincrementado automáticamente por SQLite
    // cuando inserte nuevo registros en la tabla no habrá necesidad de poner AUTO_INCREMENT asi que se omitirá
    //En SQLite, la manera de lograr la autoincrementación es
    //utilizando INTEGER PRIMARY KEY, sin necesidad de especificar AUTO_INCREMENT.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user_info table
        db.execSQL("CREATE TABLE user_info (uid TEXT PRIMARY KEY, username TEXT)");


        String createUserTable = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "lastname TEXT, " +
                "username TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "code TEXT, " +
                "is_active INTEGER DEFAULT 0, " +
                "is_admin INTEGER DEFAULT 0, " +
                "created_at DATETIME);";
        db.execSQL(createUserTable);

        // Creación de la tabla recover
        String createRecoverTable = "CREATE TABLE recover (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER, " +
                "code TEXT, " +
                "is_used INTEGER DEFAULT 0, " +
                "created_at DATETIME, " +
                "FOREIGN KEY(user_id) REFERENCES user(id));";
        db.execSQL(createRecoverTable);

        String createCountryTable = "CREATE TABLE country(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "preffix VARCHAR(50));";
        db.execSQL(createCountryTable);

        String insertCountryData = "INSERT INTO country(name, preffix) VALUES " +
                "('Mexico', 'mx'), " +
                "('Argentina', 'ar'), " +
                "('España', 'es'), " +
                "('Estados Unidos', 'eu'), " +
                "('Chile', 'cl'), " +
                "('Colombia', 'co'), " +
                "('Peru', 'pe');";
        db.execSQL(insertCountryData);

        // Creación de la tabla sentimental
        String createSentimentalTable = "CREATE TABLE sentimental(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "name VARCHAR(50));";
        db.execSQL(createSentimentalTable);

        // Inserción de datos en la tabla sentimental
        String insertSentimentalData = "INSERT INTO sentimental(name) VALUES " +
                "('Soltero'), " +
                "('Casado');";
        db.execSQL(insertSentimentalData);

        // Creación de la tabla profile
        String createProfileTable = "CREATE TABLE profile(" +
                "user_id INTEGER NOT NULL  PRIMARY KEY, " +
                "day_of_birth DATE, " +
                "gender VARCHAR(1), " +
                "country_id INTEGER, " +
                "image VARCHAR(255), " +
                "image_header VARCHAR(255), " +
                "title VARCHAR(255), " +
                "bio VARCHAR(255), " +
                "likes TEXT, " +
                "dislikes TEXT, " +
                "address VARCHAR(255), " +
                "phone VARCHAR(255), " +
                "public_email VARCHAR(255), " +
                "level_id INTEGER, " +
                "sentimental_id INTEGER, " +
                "FOREIGN KEY (sentimental_id) REFERENCES sentimental(id), " +
                "FOREIGN KEY (country_id) REFERENCES country(id), " +
                "FOREIGN KEY (level_id) REFERENCES level(id), " +
                "FOREIGN KEY (user_id) REFERENCES user(id));";
        db.execSQL(createProfileTable);

        // Creación de la tabla album
        String createAlbumTable = "CREATE TABLE album(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "title VARCHAR(200), " +
                "content VARCHAR(500), " +
                "user_id INTEGER, " +
                "level_id INTEGER, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (user_id) REFERENCES user(id), " +
                "FOREIGN KEY (level_id) REFERENCES level(id));";
        db.execSQL(createAlbumTable);

        // Creación de la tabla image
        String createImageTable = "CREATE TABLE image(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "src VARCHAR(255), " +
                "title VARCHAR(200), " +
                "content VARCHAR(500), " +
                "user_id INTEGER, " +
                "level_id INTEGER, " +
                "album_id INTEGER, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (album_id) REFERENCES album(id), " +
                "FOREIGN KEY (user_id) REFERENCES user(id), " +
                "FOREIGN KEY (level_id) REFERENCES level(id));";
        db.execSQL(createImageTable);

        // Creación de la tabla post
        String createPostTable = "CREATE TABLE post(" +
                "id INTEGER NOT NULL  PRIMARY KEY, " +
                "title VARCHAR(500), " +
                "content TEXT, " +
                "lat DOUBLE, " +
                "lng DOUBLE, " +
                "start_at DATETIME, " +
                "finish_at DATETIME, " +
                "receptor_type_id INTEGER DEFAULT 1, " +
                "author_ref_id INTEGER, " +
                "receptor_ref_id INTEGER, " +
                "level_id INTEGER, " +
                "post_type_id INTEGER DEFAULT 1, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (level_id) REFERENCES level(id));";
        db.execSQL(createPostTable);

        // Creación de la tabla post_image
        String createPostImageTable = "CREATE TABLE post_image(" +
                "post_id INTEGER, " +
                "image_id INTEGER, " +
                "FOREIGN KEY (post_id) REFERENCES post(id), " +
                "FOREIGN KEY (image_id) REFERENCES image(id));";
        db.execSQL(createPostImageTable);

        // Creación de la tabla heart
        String createHeartTable = "CREATE TABLE heart(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "type_id INTEGER DEFAULT 1, " +
                "ref_id INTEGER, " +
                "user_id INTEGER, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (user_id) REFERENCES user(id));";
        db.execSQL(createHeartTable);

        // Creación de la tabla comment
        String createCommentTable = "CREATE TABLE comment(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "type_id INTEGER, " +
                "ref_id INTEGER, " +
                "user_id INTEGER, " +
                "content TEXT, " +
                "comment_id INTEGER, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (user_id) REFERENCES user(id), " +
                "FOREIGN KEY (comment_id) REFERENCES comment(id));";
        db.execSQL(createCommentTable);

        // Creación de la tabla friend
        String createFriendTable = "CREATE TABLE friend(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "sender_id INTEGER, " +
                "receptor_id INTEGER, " +
                "is_accepted BOOLEAN DEFAULT 0, " +
                "is_readed BOOLEAN DEFAULT 0, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (sender_id) REFERENCES user(id), " +
                "FOREIGN KEY (receptor_id) REFERENCES user(id));";
        db.execSQL(createFriendTable);

        // Creación de la tabla conversation
        String createConversationTable = "CREATE TABLE conversation(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "sender_id INTEGER, " +
                "receptor_id INTEGER, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (sender_id) REFERENCES user(id), " +
                "FOREIGN KEY (receptor_id) REFERENCES user(id));";
        db.execSQL(createConversationTable);

        // Creación de la tabla message
        String createMessageTable = "CREATE TABLE message(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "content TEXT, " +
                "user_id INTEGER, " +
                "conversation_id INTEGER, " +
                "created_at DATETIME, " +
                "is_readed BOOLEAN DEFAULT 0, " +
                "FOREIGN KEY (user_id) REFERENCES user(id), " +
                "FOREIGN KEY (conversation_id) REFERENCES conversation(id));";
        db.execSQL(createMessageTable);

        // Creación de la tabla notification
        String createNotificationTable = "CREATE TABLE notification(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "not_type_id INTEGER, " +
                "type_id INTEGER, " +
                "ref_id INTEGER, " +
                "receptor_id INTEGER, " +
                "sender_id INTEGER, " +
                "is_readed BOOLEAN DEFAULT 0, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (sender_id) REFERENCES user(id), " +
                "FOREIGN KEY (receptor_id) REFERENCES user(id));";
        db.execSQL(createNotificationTable);

        // Creación de la tabla team
        String createTeamTable = "CREATE TABLE team (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "image VARCHAR(200), " +
                "title VARCHAR(200), " +
                "description VARCHAR(500), " +
                "user_id INTEGER, " +
                "status INTEGER DEFAULT 1, " +
                "created_at DATETIME, " +
                "FOREIGN KEY (user_id) REFERENCES user(id));";
        db.execSQL(createTeamTable);

    }


    // Método llamado cuando se necesita actualizar la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS SurveyResponses");
        onCreate(db);
    }

    // Método para agregar un usuario
    public void addUser(String uid, String email, String fullName, String otherDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("email", email);
        values.put("fullName", fullName);
        values.put("otherDetails", otherDetails);

        db.insert("Users", null, values);
    }

    // Método para agregar una respuesta de encuesta
    public void addSurveyResponse(String uid, int questionId, String response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("questionId", questionId);
        values.put("response", response);

        db.insert("SurveyResponses", null, values);
    }
}