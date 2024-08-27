package com.example.pruebaappredsocial;


public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String password; // Aquí el nombre debe ser coherente con el campo que esperas en el back-end

    public Usuario(String nombre, String apellido, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

    // Getters y setters si los necesitas
}

