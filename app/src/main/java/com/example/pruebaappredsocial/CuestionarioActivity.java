package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CuestionarioActivity extends AppCompatActivity {
        // Declarar todos los RadioGroups para las 9 preguntas
        private RadioGroup rgQuestion1, rgQuestion2, rgQuestion3, rgQuestion4, rgQuestion5, rgQuestion6, rgQuestion7, rgQuestion8, rgQuestion9;
        private Button btnNextPart;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cuestionario);

            // Inicializar vistas (RadioGroups)
            rgQuestion1 = findViewById(R.id.rg_question1);
            rgQuestion2 = findViewById(R.id.rg_question2);
            rgQuestion3 = findViewById(R.id.rg_question3);
            rgQuestion4 = findViewById(R.id.rg_question4);
            rgQuestion5 = findViewById(R.id.rg_question5);
            rgQuestion6 = findViewById(R.id.rg_question6);
            rgQuestion7 = findViewById(R.id.rg_question7);
            rgQuestion8 = findViewById(R.id.rg_question8);
            rgQuestion9 = findViewById(R.id.rg_question9);

            // Inicializar el botón "Siguiente"
            btnNextPart = findViewById(R.id.btn_next_part);

            // Manejar el botón "Siguiente"
            btnNextPart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateAnswers()) {
                        // Pasar a la Parte II del cuestionario
                        Intent intent = new Intent(CuestionarioActivity.this, CuestionarioParteDosActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        private boolean validateAnswers() {
            // Verificar que se haya seleccionado una opción en cada pregunta
            if (rgQuestion1.getCheckedRadioButtonId() == -1 ||
                    rgQuestion2.getCheckedRadioButtonId() == -1 ||
                    rgQuestion3.getCheckedRadioButtonId() == -1 ||
                    rgQuestion4.getCheckedRadioButtonId() == -1 ||
                    rgQuestion5.getCheckedRadioButtonId() == -1 ||
                    rgQuestion6.getCheckedRadioButtonId() == -1 ||
                    rgQuestion7.getCheckedRadioButtonId() == -1 ||
                    rgQuestion8.getCheckedRadioButtonId() == -1 ||
                    rgQuestion9.getCheckedRadioButtonId() == -1) {

                Toast.makeText(this, "Por favor responde todas las preguntas.", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }