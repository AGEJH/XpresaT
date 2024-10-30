package com.example.pruebaappredsocial;

public class FriendRequest {
    private String email_usuario;
    private String email_amigo;

    public FriendRequest(String email_usuario, String email_amigo) {
        this.email_usuario = email_usuario;
        this.email_amigo = email_amigo;
    }

    // Getters
    public String getEmail_usuario() {
        return email_usuario;
    }

    public String getEmail_amigo() {
        return email_amigo;
    }

    // Setters (opcional)
    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public void setEmail_amigo(String email_amigo) {
        this.email_amigo = email_amigo;
    }
}
