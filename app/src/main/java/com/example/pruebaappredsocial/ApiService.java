package com.example.pruebaappredsocial;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({"Content-Type: application/json"})
    @POST("/register")
    Call<ApiResponse> registerUser(@Body Usuario usuario);

    @POST("/login")
    Call<ApiResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/get_user")
    Call<ApiResponse> getUser(@Query("email") String email);

    // Endpoint para buscar usuarios por nombre o email
    @GET("/buscar_usuarios")
    Call<SearchUsersResponse> searchUsers(@Query("query") String query);

    // Endpoint para enviar solicitud de amistad
    @POST("/enviar_solicitud")
    Call<ApiResponse> sendFriendRequest(@Body FriendRequest friendRequest);


    // Endpoint para aceptar solicitud de amistad
    @POST("/aceptar_solicitud")
    Call<ResponseBody> acceptFriendRequest(@Body FriendRequest friendRequest);

    // Endpoint para obtener publicaciones del usuario y sus amigos
    @GET("/get_posts")
    Call<List<Post>> getPosts(@Query("username") String username);

    Call<ApiResponse> uploadImage(MultipartBody.Part body);
}
