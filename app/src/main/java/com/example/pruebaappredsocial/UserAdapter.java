package com.example.pruebaappredsocial;

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

    private final List<Usuario> userList;  // Cambiamos de 'User' a 'Usuario'

    public UserAdapter(List<Usuario> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario usuario = userList.get(position);  // Cambiamos de 'User' a 'Usuario'
        holder.bind(usuario);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        Button btnSendRequest;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
        }

        public void bind(Usuario usuario) {  // Cambiamos de 'User' a 'Usuario'
            tvUserName.setText(usuario.getName() + " " + usuario.getLastname());

            // Enviar solicitud de amistad
            btnSendRequest.setOnClickListener(v -> sendFriendRequest(usuario));
        }

        private void sendFriendRequest(Usuario usuario) {
            // Llamada para enviar solicitud de amistad usando Retrofit
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            // Crear el objeto FriendRequest con el email del usuario
            FriendRequest friendRequest = new FriendRequest(usuario.getEmail());

            // Llamada a la API con el objeto FriendRequest
            Call<ApiResponse> call = apiService.sendFriendRequest(friendRequest);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(itemView.getContext(), "Solicitud de amistad enviada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostrar error si la solicitud no fue exitosa
                        Toast.makeText(itemView.getContext(), "No se pudo enviar la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    // Mostrar un mensaje en caso de fallo de la solicitud
                    Toast.makeText(itemView.getContext(), "Error al enviar la solicitud de amistad: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

