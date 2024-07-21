package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnEditProfile;
    private ImageView ivProfilePicture;
    private TextView tvProfileName;
    private Button btnPersonalInfo;
    private Button btnPosts;
    private Button btnFriends;
    private LinearLayout postsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnEditProfile = findViewById(R.id.btn_edit_profile);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        tvProfileName = findViewById(R.id.tv_profile_name);
        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);
        btnPosts = findViewById(R.id.btnPosts);
        btnFriends = findViewById(R.id.btnFriends);

        // Lógica para editar perfil
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
                startActivity(editProfileIntent);
            }
        });
        /*
        // Lógica para manejar los botones de navegación
        btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a Información Personal
                Intent personalInfoIntent = new Intent(EditProfileActivity.this, PersonalInfoActivity.class);
                startActivity(personalInfoIntent);
            }
        });

        btnPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para mostrar posts (puede ser simplemente cambiar el color del botón seleccionado)
                btnPosts.setTextColor(getResources().getColor(R.color.azul_primario));
                btnPersonalInfo.setTextColor(getResources().getColor(android.R.color.black));
                btnFriends.setTextColor(getResources().getColor(android.R.color.black));
                loadPosts();
            }
        });

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a Amigos
                Intent friendsIntent = new Intent(EditProfileActivity.this, FriendsActivity.class);
                startActivity(friendsIntent);
            }
        });

        // Cargar posts iniciales
        loadPosts();
    }

    private void loadPosts() {
        // Aquí puedes agregar la lógica para cargar los posts del usuario
        // Por ahora, solo agregaremos algunos posts de ejemplo
        postsContainer.removeAllViews();
        for (int i = 0; i < 5; i++) {
            View postView = getLayoutInflater().inflate(R.layout.post_item, postsContainer, false);
            // Configurar la vista del post aquí
            postsContainer.addView(postView);
        }
        */

    }
}
