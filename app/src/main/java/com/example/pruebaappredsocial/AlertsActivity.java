package com.example.pruebaappredsocial;

import static com.example.pruebaappredsocial.RetrofitClient.retrofit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAlerts;
    private AlertAdapter alertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        recyclerViewAlerts = findViewById(R.id.recyclerViewAlerts);
        recyclerViewAlerts.setLayoutManager(new LinearLayoutManager(this));

        loadAlertsFromServer();
    }

    private void loadAlertsFromServer() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Alert>> call = apiService.getAlertsForUser(getEmailFromLocalStorage());

        call.enqueue(new Callback<List<Alert>>() {
            @Override
            public void onResponse(Call<List<Alert>> call, Response<List<Alert>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    alertAdapter = new AlertAdapter(response.body());
                    recyclerViewAlerts.setAdapter(alertAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Alert>> call, Throwable t) {
                Log.e("ERROR", "Error al cargar las alertas: " + t.getMessage());
            }
        });
    }
    private String getEmailFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }

}

