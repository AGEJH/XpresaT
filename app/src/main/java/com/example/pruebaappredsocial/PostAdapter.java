package com.example.pruebaappredsocial;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostEntity> postList;
    private Dao Dao;

    public PostAdapter(List<PostEntity> postList, Dao Dao) {
        this.postList = postList;
        this.Dao = Dao;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(postList.get(position));
    }
    public void addPost(PostEntity post) {
        postList.add(0, post); // Agregar al inicio de la lista
        notifyItemInserted(0); // Notificar que se insertó un elemento en la posición 0
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
        Button sendCommentButton, editButton, deleteButton;
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
            editButton = itemView.findViewById(R.id.editButton); // Botón de editar
            deleteButton = itemView.findViewById(R.id.deleteButton); // Botón de eliminar
        }

        public void bind(PostEntity post) {
            Context context = itemView.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

            String currentUserName = sharedPreferences.getString("name", "NombreDesconocido");
            String currentUserLastName = sharedPreferences.getString("lastname", "ApellidoDesconocido");

            tvAuthorName.setText(post.getAuthor());
            tvPostContent.setText(post.getContent());

            Glide.with(context)
                    .load(post.getUserProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .into(imageViewProfile);

            likeCount.setText(post.getLikesCount() + " Likes");

            updateLikeButtonIcon(post.isLiked());
            likeButton.setOnClickListener(v -> toggleLike(post));

            if (commentsRecyclerView.getLayoutManager() == null) {
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            if (commentAdapter == null) {
                commentAdapter = new CommentAdapter(context);
                commentsRecyclerView.setAdapter(commentAdapter);
            }
            commentAdapter.setComments(post.getComments());

            sendCommentButton.setOnClickListener(v -> {
                String commentContent = commentText.getText().toString().trim();

                if (!commentContent.isEmpty()) {
                    Comment newComment = new Comment(commentContent, currentUserName, currentUserLastName);
                    post.getComments().add(newComment);

                    commentAdapter.notifyItemInserted(post.getComments().size() - 1);
                    commentsRecyclerView.scrollToPosition(post.getComments().size() - 1);
                    commentText.setText("");
                }
            });

            // Acción de editar publicación
            editButton.setOnClickListener(v -> {
                // Abre una interfaz para editar el contenido del post
                String newContent = "Nuevo contenido del post"; // Puedes usar un diálogo para que el usuario edite
                post.setContent(newContent);

                // Actualiza en la base de datos
                AppDatabase db = AppDatabase.getInstance(itemView.getContext());
                new Thread(() -> {
                    db.postDao().updatePost(post); // Actualiza el post en la base de datos

                    // Actualiza el RecyclerView en el hilo principal
                    ((Activity) itemView.getContext()).runOnUiThread(() -> {
                        notifyItemChanged(getAdapterPosition()); // Actualiza en el RecyclerView
                    });
                }).start();
            });

            deleteButton.setOnClickListener(v -> {
                // Elimina el post en la base de datos
                AppDatabase db = AppDatabase.getInstance(itemView.getContext());
                new Thread(() -> {
                    db.postDao().deletePost(post); // Elimina el post de la base de datos

                    // Elimina del RecyclerView en el hilo principal
                    ((Activity) itemView.getContext()).runOnUiThread(() -> {
                        postList.remove(getAdapterPosition()); // Elimina el post del RecyclerView
                        notifyItemRemoved(getAdapterPosition()); // Notifica la eliminación en el RecyclerView
                    });
                }).start();
            });

            }

            private void toggleLike(PostEntity post) {
            post.setLiked(!post.isLiked());
            post.setLikesCount(post.isLiked() ? post.getLikesCount() + 1 : post.getLikesCount() - 1);
            likeCount.setText(post.getLikesCount() + " Likes");
            updateLikeButtonIcon(post.isLiked());
        }

        private void updateLikeButtonIcon(boolean isLiked) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_liked);
            } else {
                likeButton.setImageResource(R.drawable.ic_like);
            }
        }
    }
}
