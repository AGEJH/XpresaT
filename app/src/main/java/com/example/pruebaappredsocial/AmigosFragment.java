package com.example.pruebaappredsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AmigosFragment extends Fragment {
    private RecyclerView recyclerViewFriends;
    private AmigosAdapter amigosAdapter;
    private List<Amigo> friendsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        recyclerViewFriends = view.findViewById(R.id.recyclerViewFriends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener la lista de amigos (nombre y username)
        friendsList = getUserFriends();

        // Instanciar el adaptador con la lista de amigos
        amigosAdapter = new AmigosAdapter(friendsList);
        recyclerViewFriends.setAdapter(amigosAdapter);

        return view;
    }

    private List<Amigo> getUserFriends() {
        // Simulación de amigos (nombre y username)
        List<Amigo> friends = new ArrayList<>();
        friends.add(new Amigo("Amigo 1", "amigo1"));
        friends.add(new Amigo("Amigo 2", "amigo2"));
        return friends;
    }
}

