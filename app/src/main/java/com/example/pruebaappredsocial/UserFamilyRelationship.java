package com.example.pruebaappredsocial;

import android.content.Context;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFamilyRelationship {
    private ApiService apiService;
    private Context context;

    // Constructor que recibe el contexto y la instancia de ApiService
    public UserFamilyRelationship(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public void addTrustedContact(int userId, int relativeId) {
        Call<Void> call = apiService.addTrustedContact(userId, relativeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Contacto de confianza añadido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al añadir contacto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
