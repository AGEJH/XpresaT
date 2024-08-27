package com.example.pruebaappredsocial;// ApiService.java
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/register")  // Cambia esto a la ruta de tu endpoint de registro en tu API
    Call<ApiResponse> registerUser(@Body Usuario usuario);
}
