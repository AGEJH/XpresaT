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

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnBack, btnHome, btnNotifications, btnVideos, btnProfile, btnMenu;
    private TextView textViewWelcome;
    private ImageView imageViewSelected;
    private int likeCount = 0;
    private int commentCount = 0;
    private int shareCount = 0;
    private DatabaseHelper dbHelper; // Instance of your SQLiteOpenHelper class
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText postInput;
    private LinearLayout optionsLayout;
    private ImageView ImageViewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postInput = findViewById(R.id.postInput);
        optionsLayout = findViewById(R.id.optionsLayout);
        Button btnAddPhoto = findViewById(R.id.btnAddPhoto);
        imageViewSelected = findViewById(R.id.imageViewSelected);

        // Menu de navegación
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnVideos = findViewById(R.id.btnVideos);
        btnProfile = findViewById(R.id.btnProfile);
        btnMenu = findViewById(R.id.btnMenu);

        // Establecer escuchadores para los botones
        setButtonListeners();

        postInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {   // Campo para que los usuarios escriban sus publicaciones
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    optionsLayout.setVisibility(View.VISIBLE);
                } else {
                    optionsLayout.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.btnAddPhoto).setOnClickListener(new View.OnClickListener() {     // Campo para abrir archivos para cargar foto
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);  // Apertura de imágenes de galería
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageViewSelected.setVisibility(View.VISIBLE);
            Glide.with(this).load(imageUri).into(imageViewSelected);                          // Posteo de imagen cargada a la app.
        }
    }

    private void retrieveAndStoreUsername() {
        String userId = getUserIdFromLocalStorage();  // Obtener el ID del usuario desde el almacenamiento local

        if (userId != null) {
            Retrofit retrofit = RetrofitClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            // Crear la solicitud con el userId
            UserIdRequest request = new UserIdRequest(userId);
            Call<ApiResponse> call = apiService.getUsername(request);

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

    private String getUserIdFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userId", null);
    }


    /*private void storeUsernameInSQLite(String userId, String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", userId);
        values.put("username", username);
        db.insert("user_info", null, values);
        db.close();
    }*/
}
