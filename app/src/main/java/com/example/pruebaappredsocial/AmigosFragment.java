package com.example.pruebaappredsocial;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;

public class AmigosFragment extends Fragment {
     private TextView tvNoFriends, tvFriendCount;
        private LinearLayout friendsContainer;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_amigos, container, false);

            tvNoFriends = view.findViewById(R.id.tv_no_friends);
            tvFriendCount = view.findViewById(R.id.tv_friend_count);
            friendsContainer = view.findViewById(R.id.friends_container);

            // Simulación de amigos
            List<String> friends = getUserFriends();

            if (friends.isEmpty()) {
                tvNoFriends.setVisibility(View.VISIBLE);
                tvFriendCount.setText("0 amigos");
            } else {
                tvFriendCount.setText(friends.size() + " amigos");
                for (String friend : friends) {
                    TextView friendView = new TextView(getContext());
                    friendView.setText(friend);
                    friendView.setTextSize(16);
                    friendView.setTextColor(Color.BLACK);
                    friendsContainer.addView(friendView);
                }
            }

            return view;
        }

        private List<String> getUserFriends() {
            // Simulación de recuperación de amigos
            return Arrays.asList("Amigo 1", "Amigo 2"); // Vacío si no tiene amigos
        }
    }


