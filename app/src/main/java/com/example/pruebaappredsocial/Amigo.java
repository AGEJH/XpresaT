package com.example.pruebaappredsocial;

public class Amigo {
    private String nombre;
    private String username;

    public Amigo(String nombre, String username) {
        this.nombre = nombre;
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }
}