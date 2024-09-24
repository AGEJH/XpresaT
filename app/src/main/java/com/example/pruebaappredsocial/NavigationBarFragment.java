package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class NavigationBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_navigation, container, false);

        ImageButton btnHome = view.findViewById(R.id.btnHome);
        ImageButton btnNotifications = view.findViewById(R.id.btnNotifications);
        ImageButton btnVideos = view.findViewById(R.id.btnVideos);
        ImageButton btnProfile = view.findViewById(R.id.btnProfile);
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);

        setupNavigationBar(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
        return view;
    }

    private void setupNavigationBar(ImageButton btnHome, ImageButton btnNotifications, ImageButton btnVideos, ImageButton btnProfile, ImageButton btnMenu) {
        btnHome.setOnClickListener(v -> {
            resetButtonSelection(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
            btnHome.setSelected(true);  // Seleccionar el botón actual
            startActivity(new Intent(getContext(), HomeActivity.class));  // Cambiar de actividad si es necesario
        });

        btnNotifications.setOnClickListener(v -> {
            resetButtonSelection(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
            btnNotifications.setSelected(true);  // Seleccionar el botón actual
            startActivity(new Intent(getContext(), NotificationsActivity.class));  // Lanzar NotificationsActivity
        });

        btnVideos.setOnClickListener(v -> {
            resetButtonSelection(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
            btnVideos.setSelected(true);  // Seleccionar el botón actual
            startActivity(new Intent(getContext(), VideosActivity.class));  // Lanzar VideosActivity
        });

        btnProfile.setOnClickListener(v -> {
            resetButtonSelection(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
            btnProfile.setSelected(true);  // Seleccionar el botón actual
            startActivity(new Intent(getContext(), EditProfileActivity.class));  // Cambiar a ProfileActivity (no HomeActivity)
        });

        btnMenu.setOnClickListener(v -> {
            resetButtonSelection(btnHome, btnNotifications, btnVideos, btnProfile, btnMenu);
            btnMenu.setSelected(true);  // Seleccionar el botón actual
            startActivity(new Intent(getContext(), MenuconfigActivity.class));  // Lanzar MenuActivity (no HomeActivity)
        });
    }

    private void resetButtonSelection(ImageButton btnHome, ImageButton btnNotifications, ImageButton btnVideos, ImageButton btnProfile, ImageButton btnMenu) {
        // Deseleccionar todos los botones
        btnHome.setSelected(false);
        btnNotifications.setSelected(false);
        btnVideos.setSelected(false);
        btnProfile.setSelected(false);
        btnMenu.setSelected(false);
    }
}
