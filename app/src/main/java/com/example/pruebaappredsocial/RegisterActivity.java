package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContraseña, editTextRepetirContraseña;
    private Button btn_registrarse;
    private ImageButton btn_back;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);
        btn_registrarse = findViewById(R.id.btn_registrarse);
        btn_back = findViewById(R.id.btnBack);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btn_registrarse.setOnClickListener(v -> {
            if (validateRegistrationForm()) {
                registrarUsuario();
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos y asegúrese de que las contraseñas coincidan.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateRegistrationForm() {
        String correo = editTextCorreo.getText().toString().trim();
        if (!correo.endsWith("@ucol.mx")) {
            Toast.makeText(this, "Utilice un correo con la extensión '@ucol.mx'", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editTextNombre.getText().toString().trim().isEmpty() ||
                editTextApellido.getText().toString().trim().isEmpty() ||
                correo.isEmpty() ||
                editTextContraseña.getText().toString().trim().isEmpty() ||
                !editTextContraseña.getText().toString().trim().equals(editTextRepetirContraseña.getText().toString().trim())) {
            Toast.makeText(this, "Todos los campos son obligatorios y las contraseñas deben coincidir.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void registrarUsuario() {
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextCorreo.getText().toString().trim();
        String password = editTextContraseña.getText().toString().trim();

        // Crear el objeto usuario
        Usuario usuario = new Usuario(nombre, apellido, email, password);

        // Obtener la instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        // Hacer la llamada a la API
        Call<ApiResponse> call = apiService.registerUser(usuario);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    Log.d(TAG, "Respuesta del servidor: " + apiResponse.getMessage());
                    Toast.makeText(RegisterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Código de respuesta del servidor: " + response.code());
                        Log.e(TAG, "Cuerpo de la respuesta: " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Error en la respuesta del servidor: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer el cuerpo de la respuesta: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
