package com.example.pruebaappredsocial;
public class LoginRequest { //Encapsular los parámetros de inicio de sesión (email y password) en un solo objeto para ser enviado al servidor.
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
