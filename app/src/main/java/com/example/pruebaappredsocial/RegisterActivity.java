package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContraseña, editTextRepetirContraseña;
    private Button btn_registrarse;
    private DatabaseHelper dbHelper;
    private FirebaseAuth firebaseAuth;  // Firebase Authentication instance
    private FirebaseFirestore db; // Firebase Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);
        btn_registrarse = findViewById(R.id.btn_registrarse);

        btn_registrarse.setOnClickListener(v -> {
            if (validateRegistrationForm()) {
                registrarUsuario();
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente y asegúrese de que las contraseñas coincidan.", Toast.LENGTH_LONG).show();
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
        String email = editTextCorreo.getText().toString().trim();
        String password = editTextContraseña.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase user created successfully
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        guardarUsuarioEnFirestore(firebaseUser.getUid());
                    } else {
                        // Firebase registration failed
                        Toast.makeText(RegisterActivity.this, "Error en Firebase: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarUsuarioEnFirestore(String userId) {
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextCorreo.getText().toString().trim();

        // Crear un nuevo documento en la colección 'users' con el ID del usuario
        Usuario usuario = new Usuario(nombre, apellido, email);
        db.collection("users").document(userId)
                .set(usuario)
                .addOnSuccessListener(aVoid -> {
                    guardarUsuarioLocalmente(); // Call method to save the user locally
                })
                .addOnFailureListener(e -> {
                    // Firebase Firestore registration failed
                    Toast.makeText(RegisterActivity.this, "Error en Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void guardarUsuarioLocalmente() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", editTextNombre.getText().toString().trim());
        values.put("lastname", editTextApellido.getText().toString().trim());
        values.put("email", editTextCorreo.getText().toString().trim());
        values.put("password", editTextContraseña.getText().toString().trim());
        long newRowId = db.insert("user", null, values);
        db.close();

        if (newRowId == -1) {
            Toast.makeText(this, "Error al registrar el usuario localmente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario registrado correctamente en Firebase y localmente.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class Usuario {
        private String nombre;
        private String apellido;
        private String email;

        public Usuario(String nombre, String apellido, String email) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getEmail() {
            return email;
        }
    }
}
