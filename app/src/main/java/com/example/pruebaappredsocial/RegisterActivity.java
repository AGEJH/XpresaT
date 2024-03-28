package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContraseña, editTextRepetirContraseña;
    private Button buttonRegistrarse;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);
        buttonRegistrarse = findViewById(R.id.btn_registrarse);

        mAuth = FirebaseAuth.getInstance();

        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        final String nombre = editTextNombre.getText().toString().trim();
        final String apellido = editTextApellido.getText().toString().trim();
        final String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            editTextNombre.setError("Ingrese su nombre");
            return;
        }

        if (TextUtils.isEmpty(apellido)) {
            editTextApellido.setError("Ingrese su apellido");
            return;
        }

        if (TextUtils.isEmpty(correo)) {
            editTextCorreo.setError("Ingrese su correo electrónico");
            return;
        }

        if (TextUtils.isEmpty(contraseña)) {
            editTextContraseña.setError("Ingrese una contraseña");
            return;
        }

        if (TextUtils.isEmpty(repetirContraseña)) {
            editTextRepetirContraseña.setError("Repita su contraseña");
            return;
        }

        if (!contraseña.equals(repetirContraseña)) {
            editTextRepetirContraseña.setError("Las contraseñas no coinciden");
            return;
        }

        // Crear la cuenta del usuario
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Registro exitoso, redirigir a MainActivity
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                            // Redirigir a MainActivity
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish(); // Finalizar actividad actual para que no pueda regresar con el botón de retroceso
                        } else {
                            String errorMessage = "Error al registrar: " + task.getException().getMessage();
                            Log.e("Registro", errorMessage); // Registrar el error en Logcat
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
