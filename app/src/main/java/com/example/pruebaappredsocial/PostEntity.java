package com.example.pruebaappredsocial;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "posts")
public class PostEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userProfileImage; // Nueva variable para almacenar la imagen de perfil del usuario
    private String content;
    private String author;
    private long timestamp;

    private boolean isLiked; // Indica si el post ha sido "liked"
    private int likesCount; // Contador de likes

    @Ignore
    private List<Comment> comments; // Lista de comentarios asociada al post

    // Constructor completo para Room
    public PostEntity() {
        this.comments = new ArrayList<>(); // Inicializar lista vacía
    }

    @Ignore
    public PostEntity(String content, String author, long timestamp) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.userProfileImage = null; // O asigna un valor predeterminado si es necesario
        this.isLiked = false;
        this.likesCount = 0;
        this.comments = new ArrayList<>(); // Inicializar lista vacía
    }


    // Getters y setters para todos los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
