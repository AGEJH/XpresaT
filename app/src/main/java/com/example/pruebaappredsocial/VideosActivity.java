package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVideos;
    private VideoAdapter videoAdapter; // Asegúrate de crear esta clase.
    private List<Video> videoList; // Tu modelo de datos para videos
    private ImageButton btnBack, btnHome, btnNotifications, btnVideos, btnProfile, btnMenu; //Para la barra de navegación


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista de videos (puede ser una consulta a tu base de datos)
        videoList = obtenerListaDeVideos(); // Implementa este método para obtener los videos

        // Configura el adaptador
        videoAdapter = new VideoAdapter(this, videoList);
        recyclerViewVideos.setAdapter(videoAdapter);

        // Menu de navegación
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnVideos = findViewById(R.id.btnVideos);
        btnProfile = findViewById(R.id.btnProfile);
        btnMenu = findViewById(R.id.btnMenu);

        setButtonListeners();
    }
        // Método de ejemplo para obtener la lista de videos
        private List<Video> obtenerListaDeVideos () {
            // Aquí iría la lógica para obtener la lista de videos desde tu base de datos
            return new ArrayList<>(); // Reemplaza esto con la lógica real
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
                Intent intent = new Intent(VideosActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideosActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        btnVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideosActivity.this, VideosActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideosActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideosActivity.this, MenuconfigActivity.class);
                startActivity(intent);
            }
        });


    }
}
/*
Explicación:
RecyclerView: Se inicializa y se establece un LinearLayoutManager, que organiza los elementos en una lista vertical.

Lista de Videos: Se crea una lista de Video con algunos ejemplos. Aquí es donde puedes reemplazar las URLs de miniatura y
los datos de interacción con los datos reales de tu aplicación.

Adaptador: Se instancia el VideoAdapter con el contexto actual y la lista de videos, y luego se asigna al RecyclerView.

Consideraciones:
URLs de Miniatura: Debes reemplazar "url_de_miniatura_1", "url_de_miniatura_2", etc.
, con las URLs reales de tus miniaturas o con recursos locales si estás trabajando sin conexión.

Datos Reales: Puedes modificar o ampliar la lógica para obtener datos de una fuente externa, como una base de datos o un servicio web.

Layout XML: Asegúrate de que tu archivo activity_videos.xml tiene el RecyclerView con el ID recyclerViewVideos.
 */
