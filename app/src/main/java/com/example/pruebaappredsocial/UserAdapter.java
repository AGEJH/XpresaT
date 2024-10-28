package com.example.pruebaappredsocial;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<Usuario> userList;  // Lista de usuarios
    private final String currentUserEmail;  // Correo del usuario actual
    private final String currentUserName;   // Nombre del usuario actual

    // Constructor para recibir la lista de usuarios y los datos del usuario actual
    public UserAdapter(List<Usuario> userList, String currentUserEmail, String currentUserName) {
        this.userList = userList;
        this.currentUserEmail = currentUserEmail;  // Inicializa la variable de instancia
        this.currentUserName = currentUserName;    // Inicializa la variable de instancia
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario usuario = userList.get(position);
        // Aquí se pasa el usuario actual junto con el usuario de la lista
        holder.bind(usuario, currentUserEmail, currentUserName);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail;
        Button btnSendRequest;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
        }

        public void bind(Usuario usuario, String currentUserEmail, String currentUserName) {
            tvUserName.setText(usuario.getName());
            tvUserEmail.setText(usuario.getEmail());

            // Enviar solicitud de amistad
            btnSendRequest.setOnClickListener(v -> sendFriendRequest(usuario, currentUserEmail, currentUserName));
        }

        private void sendFriendRequest(Usuario usuario, String currentUserEmail, String currentUserName) {
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            // Crea la solicitud con los datos disponibles
            FriendRequest friendRequest = new FriendRequest(currentUserEmail, usuario.getEmail());

            Call<ApiResponse> call = apiService.sendFriendRequest(friendRequest);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(itemView.getContext(), "Solicitud de amistad enviada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UserAdapter", "Error en la respuesta: " + errorBody);
                            Toast.makeText(itemView.getContext(), "Error en la solicitud: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("UserAdapter", "Error al leer el cuerpo de respuesta: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Error al enviar la solicitud de amistad: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
