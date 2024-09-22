package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoPersonalFragment extends Fragment {
    private EditText etPassword;
    private Button btnVerify;
    private LinearLayout authenticationLayout;
    private LinearLayout personalInfoLayout;
    private TextView tvFullName, tvBirthDate, tvLocation;

    // Contraseña de ejemplo, más adelante se puede cambiar por la que esté en la base de datos.
    private String examplePassword = "1234";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);

        // Inicialización de vistas
        etPassword = view.findViewById(R.id.et_password);
        btnVerify = view.findViewById(R.id.btn_verify);
        authenticationLayout = view.findViewById(R.id.authentication_layout);
        personalInfoLayout = view.findViewById(R.id.personal_info_layout);
        tvFullName = view.findViewById(R.id.tv_full_name);
        tvBirthDate = view.findViewById(R.id.tv_birth_date);
        tvLocation = view.findViewById(R.id.tv_location);

        // Configurar el botón de verificación
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPassword();
            }
        });

        return view;
    }

    // Método para verificar la contraseña (solo ejemplo)
    private void verifyPassword() {
        String password = etPassword.getText().toString();
        //PARA CUANDO SE UTILIZE LA BASE DE DATOS:
      /*  // Obtén la contraseña almacenada del usuario
        String storedPassword = getUserPasswordFromDatabase(); // Método simulado
        if (password.equals(storedPassword)) {
            // Continúa con el proceso
        } */

        if (password.equals(examplePassword)) {
            // Si la contraseña es correcta, mostrar la información personal
            authenticationLayout.setVisibility(View.GONE);
            personalInfoLayout.setVisibility(View.VISIBLE);

            // Aquí cargamos la información del usuario desde la base de datos (simulado por ahora)
            loadUserInfo();
        } else {
            Toast.makeText(getActivity(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }

    // Método simulado para cargar la información del usuario
    private void loadUserInfo() {
        // Esto normalmente lo obtendrías de la base de datos
        String fullName = "Juan Pérez";
        String birthDate = "01/01/1990";
        String location = "Ciudad de México";

        tvFullName.setText(fullName);
        tvBirthDate.setText(birthDate);
        tvLocation.setText(location);
    }
}