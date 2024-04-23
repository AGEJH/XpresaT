package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

        btnQuestionnaire.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/LZ72W5ZvP4yjZHqm6"));
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);

        btn_registrarse.setOnClickListener(v -> {
            if (validateQuestionnaireCompletion() && validateRegistrationForm()) {
                if (!isEmailRegistered(editTextCorreo.getText().toString().trim())) {
                    registrarUsuario();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Cerrar esta actividad
                } else {
                    Toast.makeText(this, "Este correo ya está registrado.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos correctamente y asegúrese de haber completado el cuestionario.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateQuestionnaireCompletion() {
        SharedPreferences prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        return prefs.getBoolean("QuestionnaireCompleted", false);
    }

    private boolean validateRegistrationForm() {
        String correo = editTextCorreo.getText().toString().trim();
        if (!validateEmail(correo)) {
            Toast.makeText(this, "Utilice un correo con la extensión '@ucol.mx'", Toast.LENGTH_LONG).show();
            return false;
        }

        if (editTextNombre.getText().toString().trim().isEmpty() ||
                editTextApellido.getText().toString().trim().isEmpty() ||
                correo.isEmpty() ||
                editTextContraseña.getText().toString().trim().isEmpty() ||
                !editTextContraseña.getText().toString().trim().equals(editTextRepetirContraseña.getText().toString().trim())) {
            Toast.makeText(this, "Todos los campos son obligatorios y las contraseñas deben coincidir.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@ucol.mx");
    }

    private boolean isEmailRegistered(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { "email" };
        String selection = "email = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                "user",   // La tabla a consultar
                projection,           // Las columnas a retornar
                selection,            // Las columnas para la cláusula WHERE
                selectionArgs,        // Los valores para la cláusula WHERE
                null,         // No agrupar las filas
                null,          // No filtrar por grupos de filas
                null           // El orden del sorteo
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private void registrarUsuario() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", editTextNombre.getText().toString().trim());
        values.put("lastname", editTextApellido.getText().toString().trim());
        values.put("email", editTextCorreo.getText().toString().trim());
        // TODO: Implement password hashing for security
        values.put("password", editTextContraseña.getText().toString().trim());

        long newRowId = db.insert("user", null, values);
        db.close();

        if (newRowId == -1) {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
