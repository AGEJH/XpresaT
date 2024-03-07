package com.example.pruebaappredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

    public class MainActivity extends AppCompatActivity {

        private EditText editTextEmail, editTextPassword;
        private Button buttonLogin;
        private TextView textViewRegister; // Agregar referencia al TextView para el registro
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);
            buttonLogin = findViewById(R.id.buttonLogin);
            textViewRegister = findViewById(R.id.textViewRegister); // Obtener referencia del TextView para el registro

            mAuth = FirebaseAuth.getInstance();

            // Configurar OnClickListener para el botón de inicio de sesión
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    loginUser(email, password);
                }
            });

            // Configurar OnClickListener para el texto "registrate aquí"
            textViewRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Abrir la actividad de registro (RegisterActivity)
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Método para iniciar sesión con Firebase Authentication
        private void loginUser(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Inicio de sesión exitoso
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));

                            } else {
                                // Si el inicio de sesión falla, mostrar un mensaje de error
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
