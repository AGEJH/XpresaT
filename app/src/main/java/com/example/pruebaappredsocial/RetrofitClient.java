package com.example.pruebaappredsocial;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                /* .baseUrl("http://127.0.0.1:5000/")  // Cambia esto a la URL base de tu API (Con cable USB) */
                    .baseUrl("http://10.0.2.2:5000/")  // Cambia esto a la URL base de tu API  (Desde emulador android)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
