package com.example.pruebaappredsocial;

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

        TextView tvPostAuthor, tvPostContent, likeCount;
        ImageView imageViewProfile;
        ImageButton likeButton;
        RecyclerView commentsRecyclerView;
        EditText commentInput;
        Button sendCommentButton;
        CommentAdapter commentAdapter;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvPostAuthor = itemView.findViewById(R.id.tvPostAuthor);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            likeButton = itemView.findViewById(R.id.likeButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
            commentInput = itemView.findViewById(R.id.commentInput);
            sendCommentButton = itemView.findViewById(R.id.sendCommentButton);
        }

        public void bind(Post post) {
            tvPostAuthor.setText(post.getAuthor());
            tvPostContent.setText(post.getContent());

            Glide.with(itemView.getContext())
                    .load(post.getUserProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .into(imageViewProfile);

            likeCount.setText(post.getLikesCount() + " Likes");

            likeButton.setOnClickListener(v -> toggleLike(post));

            commentAdapter = new CommentAdapter(itemView.getContext());
            commentsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            commentsRecyclerView.setAdapter(commentAdapter);

            sendCommentButton.setOnClickListener(v -> {
                String commentText = commentInput.getText().toString();
                if (!commentText.isEmpty()) {
                    Comment newComment = new Comment(commentText);
                    post.getComments().add(newComment);
                    commentAdapter.notifyDataSetChanged();
                    commentInput.setText("");
                }
            });
        }
        private void toggleLike(Post post) {
            post.setLiked(!post.isLiked());
            post.setLikesCount(post.isLiked() ? post.getLikesCount() + 1 : post.getLikesCount() - 1);
            likeCount.setText(post.getLikesCount() + " Likes");
            // Aquí puedes hacer una llamada al servidor para actualizar el estado del "like"
        }
    }
}
