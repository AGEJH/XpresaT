package com.example.pruebaappredsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final ArrayList<Post> postList;

    public PostAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public void addPost(Post post) {
        postList.add(0, post); // Agregar el nuevo post al inicio de la lista
        notifyItemInserted(0); // Notificar al adaptador que un nuevo elemento fue insertado
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProfile;
        private TextView textViewPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewPost = itemView.findViewById(R.id.tvPostAuthor);
        }

        public void bind(Post post) {
            // Cargar la imagen de perfil usando Glide
            Glide.with(itemView.getContext())
                    .load(post.getUserProfileImage())  // URL de la imagen de perfil
                    .placeholder(R.drawable.ic_profile)  // Imagen de carga
                    .into(imageViewProfile);

            textViewPost.setText(post.getUsername());
        }
    }
}