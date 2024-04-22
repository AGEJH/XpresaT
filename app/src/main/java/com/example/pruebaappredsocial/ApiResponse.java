package com.example.pruebaappredsocial;
public class ApiResponse {
    private String message;  // Asegúrate de que los campos coincidan con los de tu JSON de respuesta

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


/*Ejemplo de llamada

Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("http://127.0.0.1:5000/")  // Usa la IP correcta y puerto de tu servidor Flask
    .addConverterFactory(GsonConverterFactory.create())
    .build();

ApiService apiService = retrofit.create(ApiService.class);
Call<ApiResponse> call = apiService.getTest();
call.enqueue(new Callback<ApiResponse>() {
    @Override
    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        if (response.isSuccessful()) {
            ApiResponse apiResponse = response.body();
            // Maneja la respuesta exitosa aquí
        }
    }

    @Override
    public void onFailure(Call<ApiResponse> call, Throwable t) {
        // Maneja el fallo aquí
    }
});

 */