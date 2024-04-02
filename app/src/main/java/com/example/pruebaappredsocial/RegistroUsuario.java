package com.example.pruebaappredsocial;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroUsuario {

    private FirebaseAuth mAuth;

    public RegistroUsuario() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void registrarUsuario(Context context, String nombre, String apellido, String correo, String contraseña, String repetirContraseña) {
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(repetirContraseña)) {
            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            enviarEmailVerificacion(context, user);
                        } else {
                            String errorMessage = "Error al registrar: " + task.getException().getMessage();
                            Log.e("Registro", errorMessage);
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void enviarEmailVerificacion(Context context, FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Se ha enviado un correo de verificación a " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Verificación", "Error al enviar correo de verificación", task.getException());
                            Toast.makeText(context, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
