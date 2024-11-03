package com.example.pruebaappredsocial;

public class FriendRequest {

    private String nombreAmigo;
    private String apellidoAmigo;
    private String email_usuario;
    private String email_amigo;


    public FriendRequest(String email_usuario, String email_amigo, String nombreAmigo, String apellidoAmigo) {
        this.email_usuario = email_usuario;
        this.email_amigo = email_amigo;
        this.nombreAmigo = nombreAmigo;
        this.apellidoAmigo = apellidoAmigo;
    }
    // Getters
    public String getEmail_usuario() {
        return email_usuario;
    }

    public String getEmail_amigo() {
        return email_amigo;
    }
    public String getNombreAmigo() {
        return nombreAmigo;
    }
    public String getApellidoAmigo() {
        return apellidoAmigo;
    }


    // Setters (opcional)
    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public void setEmail_amigo(String email_amigo) {
        this.email_amigo = email_amigo;
    }
    public void setNombreAmigo(String nombreAmigo) {
        this.nombreAmigo = nombreAmigo;
    }

    public void setApellidoAmigo(String apellidoAmigo) {
        this.apellidoAmigo = apellidoAmigo;
    }
}
