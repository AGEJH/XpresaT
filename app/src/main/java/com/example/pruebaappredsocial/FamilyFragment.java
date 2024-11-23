package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyAdapter adapter;
    private List<FamilyMember> familyList = new ArrayList<>();
    private Button btnAddFamily;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFamily);
        btnAddFamily = view.findViewById(R.id.btnAddFamily);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Configurar el RecyclerView
        adapter = new FamilyAdapter(familyList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Cargar familiares desde el servidor
        fetchFamilyList();

        // Listener para agregar familiar
        btnAddFamily.setOnClickListener(v -> {
            // Acción para agregar un nuevo familiar
            Toast.makeText(getContext(), "Agregar familiar", Toast.LENGTH_SHORT).show();
            // Aquí podrías abrir un diálogo para buscar y agregar un nuevo familiar
        });

        return view;
    }

    private void fetchFamilyList() {
        String currentUserEmail = getCurrentUserEmail(); // Método que obtendrá el correo del usuario actual
        Call<List<FamilyMember>> call = apiService.getFamilyMembers(currentUserEmail);

        call.enqueue(new Callback<List<FamilyMember>>() {
            @Override
            public void onResponse(Call<List<FamilyMember>> call, Response<List<FamilyMember>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    familyList.clear();
                    familyList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No se pudieron cargar los familiares", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FamilyMember>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar familiares", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserEmail() {
        // Aquí implementas la lógica para obtener el email del usuario actual, posiblemente desde SharedPreferences
        return "user@example.com"; // Temporal para prueba
    }
}
