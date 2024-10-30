package com.example.pruebaappredsocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.emailTextView.setText(request.getEmail_amigo());

        // Acción para aceptar solicitud
        holder.acceptButton.setOnClickListener(v -> {
            acceptFriendRequest(request);
            friendRequests.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, friendRequests.size());
        });

        // Acción para rechazar solicitud
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
        TextView emailTextView;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    private void acceptFriendRequest(FriendRequest request) {
        // Aquí puedes implementar la lógica para aceptar la solicitud de amistad
        // Usando el servicio API o cualquier otra lógica necesaria.
        // Por ejemplo:
        // ApiService.acceptFriendRequest(request);
    }

    private void rejectFriendRequest(FriendRequest request) {
        // Aquí puedes implementar la lógica para rechazar la solicitud de amistad
        // Usando el servicio API o cualquier otra lógica necesaria.
    }
}
