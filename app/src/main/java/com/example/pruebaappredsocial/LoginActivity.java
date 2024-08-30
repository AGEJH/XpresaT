package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextCorreo, editTextContraseña;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextCorreo = findViewById(R.id.editTextEmail);
        editTextContraseña = findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.buttonLogin);

        btn_login.setOnClickListener(v -> {
            if (validateLoginForm()) {
                iniciarSesion();
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateLoginForm() {
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();

        if (correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "El correo y la contraseña son obligatorios.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void iniciarSesion() {
        String email = editTextCorreo.getText().toString().trim();
        String password = editTextContraseña.getText().toString().trim();

        // Crear el objeto de petición de inicio de sesión
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Obtener la instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        // Hacer la llamada a la API
        Call<ApiResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        // Guardar los datos del usuario y navegar a la pantalla principal
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}


