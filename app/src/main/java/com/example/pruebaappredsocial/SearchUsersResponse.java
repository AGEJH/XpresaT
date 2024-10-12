package com.example.pruebaappredsocial;

import com.google.firebase.firestore.auth.User;

import java.util.List;

public class SearchUsersResponse {
    private List<User> usuarios;

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
}
