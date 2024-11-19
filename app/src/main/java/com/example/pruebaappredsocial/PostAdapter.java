package com.example.pruebaappredsocial;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final ArrayList<Post> postList;

    public PostAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public void addPost(Post post) {
        postList.add(0, post);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthorName, tvPostContent, likeCount;
        ImageView imageViewProfile;
        ImageButton likeButton;
        RecyclerView commentsRecyclerView;
        EditText commentText;
        Button sendCommentButton;
        CommentAdapter commentAdapter;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            likeButton = itemView.findViewById(R.id.likeButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
            commentText = itemView.findViewById(R.id.commentText);
            sendCommentButton = itemView.findViewById(R.id.sendCommentButton);
        }

        public void bind(Post post) {
            // Obtén el Context desde itemView
            Context context = itemView.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

            // Obtén los datos del usuario actual desde SharedPreferences
            String currentUserName = sharedPreferences.getString("name", "NombreDesconocido");
            String currentUserLastName = sharedPreferences.getString("lastname", "ApellidoDesconocido");

            // Configura el autor y contenido del post
            tvAuthorName.setText(post.getAuthor());
            tvPostContent.setText(post.getContent());

            // Configura la imagen de perfil usando Glide
            Glide.with(context)
                    .load(post.getUserProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .into(imageViewProfile);

            // Configura el contador de "likes"
            likeCount.setText(post.getLikesCount() + " Likes");

            // Actualiza el estado del botón de "like" y configura su OnClickListener
            updateLikeButtonIcon(post.isLiked());
            likeButton.setOnClickListener(v -> toggleLike(post));

            // Configura el RecyclerView de comentarios con un LayoutManager
            if (commentsRecyclerView.getLayoutManager() == null) {
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            // Inicializa el adaptador de comentarios y lo asocia al RecyclerView
            if (commentAdapter == null) {
                commentAdapter = new CommentAdapter(context);
                commentsRecyclerView.setAdapter(commentAdapter);
            }
            commentAdapter.setComments(post.getComments()); // Actualiza los datos del adaptador

            // Configura el botón para enviar comentarios
            sendCommentButton.setOnClickListener(v -> {
                Log.d("PostAdapter", "Botón de comentar clickeado");
                String commentContent = commentText.getText().toString().trim();

                if (!commentContent.isEmpty()) {
                    Log.d("PostAdapter", "Texto ingresado: " + commentContent);

                    // Usa el nombre y apellido del usuario actual obtenidos de SharedPreferences
                    Comment newComment = new Comment(commentContent, currentUserName, currentUserLastName);
                    post.getComments().add(newComment);

                    Log.d("PostAdapter", "Comentario añadido. Total comentarios: " + post.getComments().size());

                    commentAdapter.notifyItemInserted(post.getComments().size() - 1);
                    commentsRecyclerView.scrollToPosition(post.getComments().size() - 1);

                    commentText.setText("");
                } else {
                    Log.d("PostAdapter", "Campo de comentario vacío, no se agrega comentario");
                }
            });
    }

        private void toggleLike(Post post) {
            // Cambia el estado del "like" en el post
            post.setLiked(!post.isLiked());

            // Actualiza el conteo de "likes"
            post.setLikesCount(post.isLiked() ? post.getLikesCount() + 1 : post.getLikesCount() - 1);
            likeCount.setText(post.getLikesCount() + " Likes");

            // Cambia el icono del botón de "like" según el nuevo estado
            updateLikeButtonIcon(post.isLiked());

            // Aquí puedes hacer una llamada al servidor para actualizar el estado del "like"
        }

        private void updateLikeButtonIcon(boolean isLiked) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_liked); // Icono para el estado "likeado"
            } else {
                likeButton.setImageResource(R.drawable.ic_like); // Icono para el estado "no likeado"
            }
        }
    }
}
