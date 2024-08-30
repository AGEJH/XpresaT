package com.example.pruebaappredsocial;

public class ApiResponse {
    private String message;  // Asegúrarsede que los campos coincidan con los de tu JSON de respuesta
    private boolean success; // Campo que indica el éxito de la operación
    private String username; //campo para que el servidor responda
    // Getter y setter para 'message'
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter y setter para 'success'
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getUsername() {  // Añade este método
        return username;
    }

    public void setUsername(String username) {  // Añade este método
        this.username = username;
    }
}
