package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CuestionarioParteDosActivity extends AppCompatActivity {

    private RadioGroup rgQuestion1, rgQuestion2, rgQuestion3, rgQuestion4, rgQuestion5, rgQuestion6, rgQuestion7;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_parte_dos);

        // Inicializar vistas
        rgQuestion1 = findViewById(R.id.rg_question1_part2);
        rgQuestion2 = findViewById(R.id.rg_question2_part2);
        rgQuestion3 = findViewById(R.id.rg_question3_part2);
        rgQuestion4 = findViewById(R.id.rg_question4_part2);
        rgQuestion5 = findViewById(R.id.rg_question5_part2);
        rgQuestion6 = findViewById(R.id.rg_question6_part2);
        rgQuestion7 = findViewById(R.id.rg_question7_part2);
        btnSubmit = findViewById(R.id.btn_submit_part2);

        // Manejar botón de envío en la parte II
        btnSubmit.setOnClickListener(v -> {
            if (validateAnswers()) {
                // Aquí podrías manejar el envío de las respuestas o mostrar un mensaje
                Intent intent = new Intent(CuestionarioParteDosActivity.this, CuestionarioParteTresActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateAnswers() {
        if (rgQuestion1.getCheckedRadioButtonId() == -1 ||
                rgQuestion2.getCheckedRadioButtonId() == -1 ||
                rgQuestion3.getCheckedRadioButtonId() == -1 ||
                rgQuestion4.getCheckedRadioButtonId() == -1 ||
                rgQuestion5.getCheckedRadioButtonId() == -1 ||
                rgQuestion6.getCheckedRadioButtonId() == -1 ||
                rgQuestion7.getCheckedRadioButtonId() == -1) {

            Toast.makeText(this, "Por favor responde todas las preguntas.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
