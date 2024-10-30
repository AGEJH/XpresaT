package com.example.pruebaappredsocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class NotificationsActivity extends AppCompatActivity {
    private ImageButton btnBack, btnHome, btnNotifications, btnVideos, btnProfile, btnMenu;
    private RecyclerView recyclerViewFriendRequests;
    private FriendRequestAdapter adapter;
    private List<FriendRequest> friendRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Inicializar botones y RecyclerView
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnVideos = findViewById(R.id.btnVideos);
        btnProfile = findViewById(R.id.btnProfile);
        btnMenu = findViewById(R.id.btnMenu);
        recyclerViewFriendRequests = findViewById(R.id.recyclerViewFriendRequests);

        // Configurar RecyclerView
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRequestAdapter(friendRequests, this);
        recyclerViewFriendRequests.setAdapter(adapter);

        // Cargar solicitudes de amistad (Ejemplo simulado)
        loadFriendRequests();

        // Configurar escuchadores de botones
        setButtonListeners();
    }
    private void setButtonListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        btnVideos.setOnClickListener(v -> startActivity(new Intent(this, VideosActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        btnMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuconfigActivity.class)));
    }
    private void loadFriendRequests() {
        String emailUsuario = getEmailFromLocalStorage();  // Método para obtener el email del usuario actual

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<FriendRequest>> call = apiService.getFriendRequests(emailUsuario);

        call.enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendRequests.clear();
                    friendRequests.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Mostrar u ocultar el RecyclerView o el mensaje de notificaciones vacías
                    if (friendRequests.isEmpty()) {
                        recyclerViewFriendRequests.setVisibility(View.GONE);
                        findViewById(R.id.textViewNoNotifications).setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewFriendRequests.setVisibility(View.VISIBLE);
                        findViewById(R.id.textViewNoNotifications).setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(NotificationsActivity.this, "No se pudieron cargar las solicitudes de amistad", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, "Error de red al cargar las solicitudes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getEmailFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);  // Obtén el email en lugar del userId
    }
}
