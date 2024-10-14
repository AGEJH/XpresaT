package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        // Inicializar los componentes del layout
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewFriends = findViewById(R.id.recyclerViewResults);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Configurar RecyclerView
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList); // Inicializar el adaptador con una lista vacía
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
        Call<SearchUsersResponse> call = apiService.searchUsers(query); // Cambiado si usas SearchUsersResponse

        call.enqueue(new Callback<SearchUsersResponse>() {
            @Override
            public void onResponse(Call<SearchUsersResponse> call, Response<SearchUsersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body().getUsuarios()); // Obtener los usuarios de la respuesta
                    userAdapter.notifyDataSetChanged(); // Notificar al adaptador sobre los nuevos datos
                } else {
                    Toast.makeText(SearchFriendsActivity.this, "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchUsersResponse> call, Throwable t) {
                Toast.makeText(SearchFriendsActivity.this, "Error en la búsqueda: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
