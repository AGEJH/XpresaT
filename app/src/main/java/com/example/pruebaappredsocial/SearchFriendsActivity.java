package com.example.pruebaappredsocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ImageButton btnBack, btnHome, btnNotifications, btnVideos, btnProfile, btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        // Inicializar los componentes del layout
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewFriends = findViewById(R.id.recyclerViewResults);
        buttonSearch = findViewById(R.id.buttonSearch);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnVideos = findViewById(R.id.btnVideos);
        btnProfile = findViewById(R.id.btnProfile);
        btnMenu = findViewById(R.id.btnMenu);
        // Configurar escuchadores de botones
        setButtonListeners();
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
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    try {
                        searchUsers(query); // Realizar la búsqueda
                    } catch (Exception e) {
                        // Captura cualquier excepción y muestra un mensaje sin detener la actividad
                        Toast.makeText(SearchFriendsActivity.this, "Error al realizar la búsqueda", Toast.LENGTH_SHORT).show();
                        Log.e("SearchError", "Error en la búsqueda: ", e);
                    }
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
                    // Verificar que la lista de usuarios no sea null
                    List<Usuario> usuarios = response.body().getUsuarios();
                    if (usuarios != null && !usuarios.isEmpty()) {
                        // Limpiar la lista y agregar los nuevos resultados
                        userList.clear();
                        userList.addAll(usuarios);
                        userAdapter.notifyDataSetChanged(); // Actualizar el adaptador con los nuevos datos
                    } else {
                        // Mostrar mensaje si no se encontraron usuarios
                        Toast.makeText(SearchFriendsActivity.this, "No se encontró a la persona", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la respuesta no fue exitosa o no contiene datos
                    Log.e("Error", "Código de respuesta: " + response.code());
                    Toast.makeText(SearchFriendsActivity.this, "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchUsersResponse> call, Throwable t) {
                Log.e("Error", "Error en la búsqueda: " + t.getMessage());
                Toast.makeText(SearchFriendsActivity.this, "Error en la búsqueda: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setButtonListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        btnVideos.setOnClickListener(v -> startActivity(new Intent(this, VideosActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        btnMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuconfigActivity.class)));
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