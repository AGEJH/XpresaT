package com.example.pruebaappredsocial;

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

            // Verificar si las referencias no son nulas
            if (commentText == null) {
                Log.e("PostViewHolder", "commentInput es null");
            }
            if (sendCommentButton == null) {
                Log.e("PostViewHolder", "sendCommentButton es null");
            }
        }

        public void bind(Post post) {
            // Configura el autor y contenido del post
            tvAuthorName.setText(post.getAuthor());
            tvPostContent.setText(post.getContent());

            // Configura la imagen de perfil usando Glide
            Glide.with(itemView.getContext())
                    .load(post.getUserProfileImage())
                    .placeholder(R.drawable.ic_profile) // Asegúrate de tener este recurso
                    .into(imageViewProfile);

            // Configura el contador de "likes"
            likeCount.setText(post.getLikesCount() + " Likes");

            // Actualiza el estado del botón de "like" y configura su OnClickListener
            updateLikeButtonIcon(post.isLiked());
            likeButton.setOnClickListener(v -> toggleLike(post));

            // Configura el RecyclerView de comentarios con un LayoutManager
            if (commentsRecyclerView.getLayoutManager() == null) {
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            }

            // Inicializa el adaptador de comentarios y lo asocia al RecyclerView
            if (commentAdapter == null) {
                commentAdapter = new CommentAdapter(itemView.getContext());
                commentsRecyclerView.setAdapter(commentAdapter);
            }
            commentAdapter.setComments(post.getComments()); // Actualiza los datos del adaptador

            Log.d("PostAdapter", "Adaptador de comentarios configurado correctamente");

            // Verifica que los componentes de comentario no sean nulos
            if (sendCommentButton == null || commentText == null) {
                Log.e("PostAdapter", "sendCommentButton o commentText son nulos en bind()");
                return; // Salimos temprano si hay un problema
            }

            // Configura el botón para enviar comentarios
            sendCommentButton.setOnClickListener(v -> {
                Log.d("PostAdapter", "Botón de comentar clickeado");
                String commentContent = commentText.getText().toString().trim(); // Usa trim para eliminar espacios innecesarios

                if (!commentContent.isEmpty()) {
                    Log.d("PostAdapter", "Texto ingresado: " + commentContent);
                    // Usa datos del usuario actual como nombre y apellido del autor
                    String authorName = "NombreUsuario"; // Reemplaza con el nombre real del usuario
                    String authorLastName = "ApellidoUsuario"; // Reemplaza con el apellido real del usuario

                    Comment newComment = new Comment(commentContent, authorName, authorLastName);
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
