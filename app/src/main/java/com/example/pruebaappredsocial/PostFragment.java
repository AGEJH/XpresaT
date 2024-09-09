package com.example.pruebaappredsocial;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class PostFragment extends Fragment {
    private TextView tvNoPosts;
    private LinearLayout postsContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        tvNoPosts = view.findViewById(R.id.tv_no_posts);
        postsContainer = view.findViewById(R.id.posts_container);

        // Simulación de publicaciones
        List<String> posts = getUserPosts();

        if (posts.isEmpty()) {
            // Si no hay publicaciones, muestra el mensaje y oculta el contenedor de publicaciones
            tvNoPosts.setVisibility(View.VISIBLE);
            postsContainer.setVisibility(View.GONE);
        } else {
            // Si hay publicaciones, oculta el mensaje y muestra las publicaciones
            tvNoPosts.setVisibility(View.GONE);
            postsContainer.setVisibility(View.VISIBLE);
            for (String post : posts) {
                TextView postView = new TextView(getContext());
                postView.setText(post);
                postView.setTextSize(16);
                postView.setTextColor(Color.BLACK);
                postView.setPadding(0, 10, 0, 10);
                postsContainer.addView(postView);
            }
        }

        return view;
    }

    private List<String> getUserPosts() {
        // Simulación de recuperación de publicaciones
        return Arrays.asList("Post 1", "Post 2", "Post 3"); // Vacío si no tiene publicaciones
    }
}

