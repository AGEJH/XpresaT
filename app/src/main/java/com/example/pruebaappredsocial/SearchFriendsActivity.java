package com.example.pruebaappredsocial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFriendsActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private RecyclerView recyclerViewFriends;
    private Button buttonSearch;
    private UserAdapter userAdapter; // Para mostrar los resultados de búsqueda
    private List<Usuario> userList = new ArrayList<>(); // Lista de resultados de búsqueda
    private String currentUserEmail; // Inicializa con el email del usuario actual
    private String currentUserName;  // Inicializa con el nombre del usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        // Inicializar los componentes del layout
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewFriends = findViewById(R.id.recyclerViewResults);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Obtener el email y nombre del usuario actual (implementa esta lógica según tu app)
        currentUserEmail = getCurrentUserEmail();
        currentUserName = getCurrentUserName();

        if (currentUserEmail == null) {
            Log.e("Error", "Email del usuario actual no se encontró en SharedPreferences");
        } else {
            Log.d("Info", "Email del usuario actual: " + currentUserEmail);
        }

        // Configurar RecyclerView
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, currentUserEmail, currentUserName); // Inicializar el adaptador
        recyclerViewFriends.setAdapter(userAdapter);

        // Listener del botón de búsqueda
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextSearch.getText().toString();
                if (!query.isEmpty()) {
                    searchUsers(query); // Realizar la búsqueda
                } else {
                    Toast.makeText(SearchFriendsActivity.this, "Por favor, ingrese un nombre o email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener para detectar cambios en el campo de búsqueda (opcional si quieres búsqueda en tiempo real)
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Aquí puedes implementar una búsqueda en tiempo real si deseas
                // searchUsers(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Lógica para buscar usuarios en la base de datos a través del servidor
    private void searchUsers(String query) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<SearchUsersResponse> call = apiService.searchUsers(query);

        call.enqueue(new Callback<SearchUsersResponse>() {
            @Override
            public void onResponse(Call<SearchUsersResponse> call, Response<SearchUsersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Imprimir la respuesta completa en JSON para depurar
                    Log.d("RespuestaCompleta", new Gson().toJson(response.body()));

                    // Recorrer la lista de usuarios y mostrar su información
                    for (Usuario usuario : response.body().getUsuarios()) {
                        Log.d("Usuario", "Nombre: " + usuario.getName() + ", Apellido: " + usuario.getLastname());
                    }

                    userList.clear();
                    userList.addAll(response.body().getUsuarios());
                    userAdapter.notifyDataSetChanged(); // Actualizar el adaptador con los nuevos datos
                } else {
                    // Si no se encontraron usuarios, imprime el código de respuesta para mayor información
                    Log.e("Error", "Código de respuesta: " + response.code());
                    Toast.makeText(SearchFriendsActivity.this, "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchUsersResponse> call, Throwable t) {
                // Imprimir el mensaje de error para depuración
                Log.e("Error", "Error en la búsqueda: " + t.getMessage());
                Toast.makeText(SearchFriendsActivity.this, "Error en la búsqueda: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserEmail() {
        // Llama a `getEmailFromLocalStorage` en HomeActivity para obtener el email desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null); // Obtén el email en lugar del valor estático
    }

    private String getCurrentUserName() {
        // Implementa la lógica de obtención del nombre, similar al email, si es necesario.
        return "Nombre de Usuario";
    }
}