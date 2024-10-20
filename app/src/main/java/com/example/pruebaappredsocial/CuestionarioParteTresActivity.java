package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class CuestionarioParteTresActivity extends AppCompatActivity {

    // Declarar los campos de texto
    private EditText editTextFeliz, editTextSufre, editTextPropioCaso;
    private Button btnSubmitParteIII;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_parte_tres);

        // Inicializar vistas
        editTextFeliz = findViewById(R.id.edit_text_feliz);
        editTextSufre = findViewById(R.id.edit_text_sufre);
        editTextPropioCaso = findViewById(R.id.edit_text_propio_caso);
        btnSubmitParteIII = findViewById(R.id.btn_submit_parte_iii);

        // Configurar botón de envío
        btnSubmitParteIII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAnswers()) {
                    // Pasar a la Parte II del cuestionario
                    Intent intent = new Intent(CuestionarioParteTresActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Validar que el usuario haya respondido todas las preguntas
    private boolean validateAnswers() {
        if (editTextFeliz.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor responde quién es más feliz.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextSufre.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor responde quién sufre más.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextPropioCaso.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor describe tu propio caso.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

