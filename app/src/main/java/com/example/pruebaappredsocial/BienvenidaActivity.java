package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HomeActivity", "onCreate: HomeActivity creada");
        setContentView(R.layout.activity_bienvenida);
            TextView textView = findViewById(R.id.tvSignUp);
        textView.setText(Html.fromHtml("<u>¡Crea una aquí!</u>"));

        // Configurar el TextView para manejar el click
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
        // Configurar el Button para manejar el click
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
        // Cargar imagen circular con Glide en el ImageView
        ImageView ivLogo = findViewById(R.id.ivLogo);
        Glide.with(this)
                .load(R.drawable.xpresat5)  // Carga la imagen desde los recursos
                .circleCrop()                // Aplica la transformación circular
                .into(ivLogo);               // Coloca la imagen en el ImageView
    }
    private void navigateToRegister() {
        // Navegar a la actividad de registro
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void navigateToLogin() {
        // Navegar a la actividad de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}