package com.example.pruebaappredsocial;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/register")
    Call<ApiResponse> registerUser(@Body Usuario usuario);

    @POST("/login")
    Call<ApiResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/get_user")
    Call<ApiResponse> getUser(@Query("email") String email);

}
