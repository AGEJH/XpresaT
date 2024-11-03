package com.example.pruebaappredsocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        // Configurar nombre y apellido del amigo
        holder.friendNameTextView.setText(request.getNombreAmigo());
        holder.friendLastNameTextView.setText(request.getApellidoAmigo());

        // Aquí puedes cargar la imagen del ícono del amigo, si tienes una URL o recurso
        // Por ejemplo, usando Glide o Picasso:

        // Configura los botones de aceptar y rechazar
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
        // Implementa la lógica para aceptar la solicitud
    }

    private void rejectFriendRequest(FriendRequest request) {
        // Implementa la lógica para rechazar la solicitud
    }
}