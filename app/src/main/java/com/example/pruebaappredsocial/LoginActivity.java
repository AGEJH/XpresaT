package com.example.pruebaappredsocial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextCorreo, editTextContraseña;
    private Button btn_login;
    private TextView textViewRegister;

    private ImageButton btn_back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_back = findViewById(R.id.btnBack);
        editTextCorreo = findViewById(R.id.editTextEmail);
        editTextContraseña = findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, BienvenidaActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
            if (validateLoginForm()) {
                iniciarSesion();
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
            }
        });
        // Cargar imagen circular con Glide en el ImageView
        ImageView imageViewLogo = findViewById(R.id.imageViewLogo);
        Glide.with(this)
                .load(R.drawable.xpresat5)  // Carga la imagen desde los recursos
                .circleCrop()                // Aplica la transformación circular
                .into(imageViewLogo);               // Coloca la imagen en el ImageView
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
                    Log.d("LoginActivity", "Respuesta del servidor: " + apiResponse.getMessage() + ", Success: " + apiResponse.isSuccess());

                    if (apiResponse.isSuccess()) {
                        // Guardar los datos del usuario y navegar a la pantalla principal
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        ArrayList<Post> postList = new ArrayList<>(); // Supón que aquí llenas tu lista de publicaciones

                        // Aquí podrías llenar postList con los datos que tengas
                        long timestamp = System.currentTimeMillis(); // Obtener la marca de tiempo actual
                        postList.add(new Post("Este es un post", "Autor1", timestamp));
                        postList.add(new Post("Otro post", "Autor2", timestamp));

                        intent.putParcelableArrayListExtra("posts", postList);
                        startActivity(intent);
                        finish();
                    } else {
                        // Mostrar el mensaje del servidor
                        Toast.makeText(LoginActivity.this, "Error: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    int errorCode = response.code();
                    String errorBody = null;
                    if (response.errorBody() != null) {
                        try {
                            errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("LoginActivity", "Error en la respuesta del servidor. Código: " + errorCode + ", Detalles: " + errorBody);
                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor. Código: " + errorCode + ", Detalles: " + errorBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("LoginActivity", "Error de red: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}


