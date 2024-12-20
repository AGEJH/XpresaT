package com.example.pruebaappredsocial;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PostEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Dao Dao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database").build();
        }
        return INSTANCE;
    }
}
