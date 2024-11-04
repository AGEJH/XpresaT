package com.example.pruebaappredsocial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private List<FriendRequest> friendRequests;
    private Context context;

    public FriendRequestAdapter(List<FriendRequest> friendRequests, Context context) {
        this.friendRequests = friendRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest request = friendRequests.get(position);
        Log.d("FriendRequestAdapter", "Nombre: " + request.getNombreAmigo() + ", Email: " + request.getEmail_amigo());

        holder.friendNameTextView.setText(request.getNombreAmigo());
        holder.friendLastNameTextView.setText(request.getApellidoAmigo());

        holder.acceptButton.setOnClickListener(v -> {
            acceptFriendRequest(request);
            friendRequests.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, friendRequests.size());
        });

        holder.rejectButton.setOnClickListener(v -> {
            rejectFriendRequest(request);
            friendRequests.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, friendRequests.size());
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendIconImageView;
        TextView friendNameTextView, friendLastNameTextView;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendIconImageView = itemView.findViewById(R.id.friendIconImageView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
            friendLastNameTextView = itemView.findViewById(R.id.friendLastNameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    private void acceptFriendRequest(FriendRequest request) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.acceptFriendRequest(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Solicitud de amistad aceptada", Toast.LENGTH_SHORT).show();
                    updateFriendRequestsList();
                } else {
                    Toast.makeText(context, "Error al aceptar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectFriendRequest(FriendRequest request) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.rejectFriendRequest(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String message = response.body().string();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        updateFriendRequestsList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error al rechazar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFriendRequestsList() {
        // Actualiza la lista de solicitudes de amistad según la lógica de tu aplicación
    }
}
