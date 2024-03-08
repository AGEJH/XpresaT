package com.example.pruebaappredsocial;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageViewNotifications, imageViewFriends, imageViewMenu, imageViewSearchIcon;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        // Inicialización de las vistas
        imageViewNotifications = findViewById(R.id.imageViewNotifications);
        imageViewFriends = findViewById(R.id.imageViewFriends);
        imageViewMenu = findViewById(R.id.imageViewMenu);
        imageViewSearchIcon = findViewById(R.id.imageViewSearchIcon);
        editTextSearch = findViewById(R.id.editTextSearch);

        // Aquí puedes agregar cualquier lógica adicional que necesites
    }
}
