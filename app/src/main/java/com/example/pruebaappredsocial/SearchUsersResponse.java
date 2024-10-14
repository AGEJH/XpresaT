package com.example.pruebaappredsocial;

import java.util.List;

public class SearchUsersResponse {
    private List<Usuario> usuarios;  // Cambiar 'User' por 'Usuario'

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
