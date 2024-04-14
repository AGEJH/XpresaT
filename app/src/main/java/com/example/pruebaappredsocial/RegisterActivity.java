package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContraseña, editTextRepetirContraseña;
    private Button btn_registrarse;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);
        Button btnQuestionnaire = findViewById(R.id.btnQuestionnaire);
        btn_registrarse = findViewById(R.id.btn_registrarse);
        btnQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplaza "url_del_cuestionario" con tu URL específica
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/LZ72W5ZvP4yjZHqm6"));
                startActivity(intent);
            }
        });


        dbHelper = new DatabaseHelper(this);

        // Maneja el evento de clic en el botón de registro
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);
                boolean isQuestionnaireCompleted = prefs.getBoolean("QuestionnaireCompleted", false);

                if (isQuestionnaireCompleted) {
                    // Aquí iría el código para guardar los datos del usuario en la base de datos
                    // Suponiendo que el registro es exitoso, iniciamos la actividad de inicio de sesión
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Esto cierra la actividad de registro para que el usuario no pueda volver presionando el botón atrás
                } else {
                    // Mostrar un mensaje al usuario indicando que necesita completar el cuestionario
                    Toast.makeText(RegisterActivity.this, "Por favor, complete el cuestionario para continuar con el registro.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrarUsuario() {
        // Obtiene los datos del formulario
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        // Abre la base de datos en modo escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un nuevo registro de usuario
        ContentValues values = new ContentValues();
        values.put("name", nombre);
        values.put("lastname", apellido);
        values.put("email", correo);
        values.put("password", contraseña);

        // Inserta el registro en la tabla 'user'
        long newRowId = db.insert("user", null, values);

        // Cierra la conexión de la base de datos
        dbHelper.close();

        // Lógica adicional después del registro...
    }
}
