package com.example.pruebaappredsocial;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert
    void insertPost(PostEntity post);

    @Query("SELECT * FROM posts")
    List<PostEntity> getAllPosts();

    // Método para actualizar un post existente
    @Update
    void updatePost(PostEntity post);

    // Método para eliminar un post específico
    @Delete
    void deletePost(PostEntity post);
}