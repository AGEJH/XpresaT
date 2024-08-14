package com.example.pruebaappredsocial;

public class Video {
    private String nombreUsuario;
    private String descripcion;
    private String urlMiniatura;
    private int numLikes;
    private int numComentarios;
    private int numCompartidos;

    public Video(String nombreUsuario, String descripcion, String urlMiniatura, int numLikes, int numComentarios, int numCompartidos) {
        this.nombreUsuario = nombreUsuario;
        this.descripcion = descripcion;
        this.urlMiniatura = urlMiniatura;
        this.numLikes = numLikes;
        this.numComentarios = numComentarios;
        this.numCompartidos = numCompartidos;
    }

    // Getters y setters

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrlMiniatura() {
        return urlMiniatura;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public int getNumComentarios() {
        return numComentarios;
    }

    public int getNumCompartidos() {
        return numCompartidos;
    }
}
