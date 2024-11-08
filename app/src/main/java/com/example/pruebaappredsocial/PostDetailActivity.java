package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private TextView postContent;
    private ImageButton likeButton;
    private TextView likesCount;
    private RecyclerView commentsRecyclerView;
    private EditText commentInput;
    private Button sendCommentButton;
    private ApiService apiService;
    private CommentAdapter commentAdapter;
    private int postId;
    private int likes = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Inicializar vistas
        postContent = findViewById(R.id.post_content);
        likeButton = findViewById(R.id.like_button);
        likesCount = findViewById(R.id.like_count);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentInput = findViewById(R.id.comment_input);
        sendCommentButton = findViewById(R.id.send_comment_button);

        // Configurar RecyclerView
        commentAdapter = new CommentAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Inicializar ApiService
        apiService = RetrofitClient.getApiService();

        // Obtener el ID del post (debe ser pasado desde el Intent)
        postId = getIntent().getIntExtra("POST_ID", -1);

        if (postId == -1) {
            // Mostrar mensaje de error si el postId es inválido y detener la carga
            Toast.makeText(this, "Error: ID de post inválido", Toast.LENGTH_SHORT).show();
            postContent.setText("Post no disponible");
            return;
        }

        // Cargar detalles del post y comentarios
        loadPostDetails();

        // Configurar botón de Like
        likeButton.setOnClickListener(v -> toggleLike());

        // Configurar botón para añadir comentario
        sendCommentButton.setOnClickListener(v -> addComment());
    }

    private void loadPostDetails() {
        apiService.getPostDetails(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    postContent.setText(post.getContent());
                    likes = post.getLikesCount();
                    likesCount.setText(likes + " likes");
                    loadComments();
                } else {
                    Toast.makeText(PostDetailActivity.this, "Error al cargar detalles del post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Fallo al cargar los detalles del post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLike() {
        apiService.likePost(postId).enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LikeResponse likeResponse = response.body();
                    likes = likeResponse.getLikesCount();
                    likesCount.setText(likes + " likes");
                    Toast.makeText(PostDetailActivity.this, "Estado de 'Like' actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostDetailActivity.this, "No se pudo actualizar el estado de 'Like'", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Error al actualizar 'Like'", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        apiService.getComments(postId).enqueue(new Callback<List<com.example.pruebaappredsocial.Comment>>() {
            @Override
            public void onResponse(Call<List<com.example.pruebaappredsocial.Comment>> call, Response<List<com.example.pruebaappredsocial.Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.pruebaappredsocial.Comment> comments = response.body();
                    commentAdapter.setComments(comments);  // Asegúrate de que `setComments` en `CommentAdapter` usa List<Comment>
                } else {
                    Toast.makeText(PostDetailActivity.this, "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.example.pruebaappredsocial.Comment>> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Error de conexión al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComment() {
        String content = commentInput.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.addComment(postId, content).enqueue(new Callback<com.example.pruebaappredsocial.Comment>() {
            @Override
            public void onResponse(Call<com.example.pruebaappredsocial.Comment> call, Response<com.example.pruebaappredsocial.Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.pruebaappredsocial.Comment newComment = response.body();
                    commentAdapter.addComment(newComment); // Asegúrate de que `addComment` en `CommentAdapter` usa `Comment`
                    commentInput.setText(""); // Limpiar el campo después de enviar
                    commentsRecyclerView.scrollToPosition(commentAdapter.getItemCount() - 1); // Ir al último comentario
                } else {
                    Toast.makeText(PostDetailActivity.this, "No se pudo añadir el comentario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.pruebaappredsocial.Comment> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Error al añadir comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
