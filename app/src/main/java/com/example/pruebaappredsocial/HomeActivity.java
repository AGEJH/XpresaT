package com.example.pruebaappredsocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeActivity extends AppCompatActivity {

    // Definir variables globales
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnBack, btnHome, btnNotifications, btnVideos, btnProfile, btnMenu;
    private TextView textViewWelcome, tvNoPosts;
    private ImageView imageViewSelected;
    private EditText postInput;
    private LinearLayout optionsLayout;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>(); // Lista de publicaciones
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
   private Button btnAddPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postInput = findViewById(R.id.postInput);
        optionsLayout = findViewById(R.id.optionsLayout);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        tvNoPosts = findViewById(R.id.tv_no_posts);
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);

        // Configurar el RecyclerView
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        // Recibir el ArrayList<Post> de la actividad anterior
        ArrayList<Post> postList = getIntent().getParcelableArrayListExtra("posts");

        // Comprobar si la lista de posts no es nula
        if (postList != null) {
            postAdapter = new PostAdapter(postList); // Usar la lista recibida
        } else {
            postAdapter = new PostAdapter(new ArrayList<>()); // Crear una lista vacía si no hay posts
        }

        recyclerViewPosts.setAdapter(postAdapter);

        // Cargar publicaciones (esto puede ser opcional si ya tienes publicaciones al iniciar)
        loadPosts();

        // Menú de navegación
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnVideos = findViewById(R.id.btnVideos);
        btnProfile = findViewById(R.id.btnProfile);
        btnMenu = findViewById(R.id.btnMenu);

        // Establecer escuchadores para los botones
        setButtonListeners();


        postInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    optionsLayout.setVisibility(View.VISIBLE);
                } else {
                    optionsLayout.setVisibility(View.GONE);
                }
            }
        });

        // Configurar el OnClickListener para cambiar la visibilidad del botón según el texto
        postInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton(v);
            }
        });
    }

    // Lógica para manejar la visibilidad del botón
    public void onClickButton(View v) {
        // Obtener el texto del campo de entrada
        String inputText = postInput.getText().toString().trim();

        // Verificar si el campo de entrada está vacío
        if (inputText.isEmpty()) {
            btnAddPhoto.setVisibility(View.GONE); // Ocultar el botón si el campo está vacío
        } else {
            btnAddPhoto.setVisibility(View.VISIBLE); // Mostrar el botón si hay texto
            // También puedes manejar otras vistas, como recyclerViewPosts y tvNoPosts
            // recyclerViewPosts.setVisibility(View.VISIBLE);
            // tvNoPosts.setVisibility(View.GONE);
        }
    }

    private void loadPosts() {
        // Aquí es donde deberías cargar las publicaciones de tu base de datos o API
        // Por ahora lo dejaremos vacío para simular que no hay publicaciones

        if (postList.isEmpty()) {
            recyclerViewPosts.setVisibility(View.GONE);
            tvNoPosts.setVisibility(View.VISIBLE);
        } else {
            recyclerViewPosts.setVisibility(View.VISIBLE);
            tvNoPosts.setVisibility(View.GONE);
        }
    }

    private void setButtonListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Volver a la actividad anterior
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        btnVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, VideosActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MenuconfigActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageViewSelected.setVisibility(View.VISIBLE);
            Glide.with(this).load(imageUri).into(imageViewSelected);
        }
    }

    private void retrieveAndStoreUsername() {
        String email = getEmailFromLocalStorage();  // Obtener el email del usuario desde el almacenamiento local

        if (email != null) {
            Retrofit retrofit = RetrofitClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            // Llama a la API utilizando el email del usuario
            Call<ApiResponse> call = apiService.getUser(email);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String username = response.body().getUsername();
                        textViewWelcome.setText("Bienvenido a XpresaT " + username);
                    } else {
                        textViewWelcome.setText("Bienvenido a XpresaT (nombre de usuario no encontrado)");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("HomeActivity", "Error al obtener el nombre del usuario", t);
                    Toast.makeText(HomeActivity.this, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            textViewWelcome.setText("Bienvenido a XpresaT (usuario no encontrado)");
        }
    }

    private String getEmailFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);  // Obtén el email en lugar del userId
    }
}
