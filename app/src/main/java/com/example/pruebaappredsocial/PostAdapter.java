package com.example.pruebaappredsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
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
        private final TextView tvPostContent;
        private final TextView tvPostAuthor;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            tvPostAuthor = itemView.findViewById(R.id.tvPostAuthor);
        }

        public void bind(Post post) {
            tvPostContent.setText(post.getContent());
            tvPostAuthor.setText(post.getAuthor());
        }
    }
}
