package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

        dbHelper = new DatabaseHelper(this); // Asegúrarse de que dbHelper se inicializa aquí.

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);
        btn_registrarse = findViewById(R.id.btn_registrarse);

        btn_registrarse.setOnClickListener(v -> {
            if (validateRegistrationForm()) {
                registrarUsuario();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Cerrar esta actividad después del registro exitoso
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente y asegúrese de haber completado el cuestionario.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateRegistrationForm() {
        String correo = editTextCorreo.getText().toString().trim();
        if (!correo.endsWith("@ucol.mx")) {
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

    private void registrarUsuario() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", editTextNombre.getText().toString().trim());
        values.put("lastname", editTextApellido.getText().toString().trim());
        values.put("email", editTextCorreo.getText().toString().trim());
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
