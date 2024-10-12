package com.example.pruebaappredsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.FriendViewHolder> {

    private List<Amigo> friendsList;

    // Constructor del adaptador que recibe la lista de amigos
    public AmigosAdapter(List<Amigo> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño item_amigo.xml para cada ítem de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        // Obtener el objeto Amigo de la lista
        Amigo amigo = friendsList.get(position);

        // Vincular el nombre y username del amigo con los TextViews correspondientes
        holder.textViewFriendName.setText(amigo.getNombre());
        holder.textViewFriendUsername.setText(amigo.getUsername());

        // Opcional: Puedes implementar un OnClickListener si quieres que cada ítem haga algo al ser pulsado
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Amigo seleccionado: " + amigo.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriendName;
        TextView textViewFriendUsername;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFriendName = itemView.findViewById(R.id.tvAmigoNombre);
            textViewFriendUsername = itemView.findViewById(R.id.tvAmigoUsername);
        }
    }
}
