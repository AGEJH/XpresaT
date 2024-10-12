package com.example.pruebaappredsocial;

public class FriendRequest {
    private int usuario_id; // ID del usuario que envía la solicitud
    private int amigo_id;   // ID del usuario que recibe la solicitud

    // Constructor
    public FriendRequest(int usuario_id, int amigo_id) {
        this.usuario_id = usuario_id;
        this.amigo_id = amigo_id;
    }

    // Getters y Setters
    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getAmigo_id() {
        return amigo_id;
    }

    public void setAmigo_id(int amigo_id) {
        this.amigo_id = amigo_id;
    }
}
