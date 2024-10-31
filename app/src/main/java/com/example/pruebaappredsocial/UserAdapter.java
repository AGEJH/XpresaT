package com.example.pruebaappredsocial;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

            Log.d("UserAdapter", "Email del usuario actual: " + currentUserEmail);
            Log.d("UserAdapter", "Email del usuario en la lista: " + usuario.getEmail());

            // Verifica el estado de amistad
            if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
                checkFriendStatus(usuario, currentUserEmail);
            }
            // Configura el listener para enviar solicitud de amistad
            btnSendRequest.setOnClickListener(v -> sendFriendRequest(usuario, currentUserEmail));
        }

        private void checkFriendStatus(Usuario usuario, String currentUserEmail) {
            if (currentUserEmail == null || currentUserEmail.isEmpty() || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
                Log.e("UserAdapter", "Emails vacíos, no se puede verificar el estado de amistad.");
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<FriendStatusResponse> call = apiService.getFriendStatus(currentUserEmail, usuario.getEmail());

            call.enqueue(new Callback<FriendStatusResponse>() {
                @Override
                public void onResponse(Call<FriendStatusResponse> call, Response<FriendStatusResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = String.valueOf(response.body().isRequestSent());
                        if ("sent".equals(status)) {
                            btnSendRequest.setText("Solicitud enviada");
                            btnSendRequest.setEnabled(false);
                        } else if ("accepted".equals(status)) {
                            btnSendRequest.setVisibility(View.GONE);
                            tvUserName.setText("Amigo");
                        } else {
                            btnSendRequest.setText("Enviar solicitud");
                            btnSendRequest.setEnabled(true);
                        }
                    } else {
                        Log.e("UserAdapter", "Error al verificar el estado de amistad");
                    }
                }

                @Override
                public void onFailure(Call<FriendStatusResponse> call, Throwable t) {
                    Log.e("UserAdapter", "Fallo en la solicitud de estado de amistad: " + t.getMessage());
                }
            });
        }

        private void sendFriendRequest(Usuario usuario, String currentUserEmail) {
            if (currentUserEmail == null || currentUserEmail.isEmpty() || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
                Log.e("UserAdapter", "Emails vacíos, no se puede enviar solicitud de amistad.");
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            FriendRequest friendRequest = new FriendRequest(currentUserEmail, usuario.getEmail());

            Call<ApiResponse> call = apiService.sendFriendRequest(friendRequest);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        btnSendRequest.setText("Solicitud enviada");
                        btnSendRequest.setEnabled(false);
                        btnSendRequest.setBackgroundTintList(
                                ContextCompat.getColorStateList(itemView.getContext(), R.color.black)
                        );
                        Toast.makeText(itemView.getContext(), "Solicitud de amistad enviada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("UserAdapter", "Error en la respuesta de solicitud de amistad.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("UserAdapter", "Error al enviar la solicitud de amistad: " + t.getMessage());
                }
            });
        }
    }
}