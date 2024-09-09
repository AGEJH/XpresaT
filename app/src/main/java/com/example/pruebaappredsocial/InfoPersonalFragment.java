package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoPersonalFragment extends Fragment {
    private LinearLayout authenticationLayout, personalInfoLayout;
    private EditText etPassword;
    private Button btnVerify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);

        authenticationLayout = view.findViewById(R.id.authentication_layout);
        personalInfoLayout = view.findViewById(R.id.personal_info_layout);
        etPassword = view.findViewById(R.id.et_password);
        btnVerify = view.findViewById(R.id.btn_verify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                if (password.equals("password_correcta")) { // Simulación de validación
                    authenticationLayout.setVisibility(View.GONE);
                    personalInfoLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
