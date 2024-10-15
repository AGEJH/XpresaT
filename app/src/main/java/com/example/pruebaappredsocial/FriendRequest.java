package com.example.pruebaappredsocial;

public class FriendRequest {
    private String email_usuario;
    private String nombre_usuario;
    private String email_amigo;
    private String nombre_amigo;

    public FriendRequest(String email_usuario, String nombre_usuario, String email_amigo, String nombre_amigo) {
        this.email_usuario = email_usuario;
        this.nombre_usuario = nombre_usuario;
        this.email_amigo = email_amigo;
        this.nombre_amigo = nombre_amigo;
    }

    // Getters y setters
}
