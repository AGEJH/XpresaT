package com.example.pruebaappredsocial;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/test")  // Asegúrate de ajustar este endpoint a uno existente en tu Flask API
    Call<ApiResponse> getTest();
}
