package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageViewNotifications, imageViewFriends, imageViewMenu, imageViewLupa;
    private LinearLayout searchBar;
    private TextView textViewWelcome;
    private DatabaseHelper dbHelper; // Instance of your SQLiteOpenHelper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicialización de las vistas
        imageViewNotifications = findViewById(R.id.imageViewNotifications);
        imageViewFriends = findViewById(R.id.imageViewFriends);
        imageViewMenu = findViewById(R.id.imageViewMenu);
        imageViewLupa = findViewById(R.id.imageViewlupa);
        textViewWelcome = findViewById(R.id.textViewWelcome);
        LinearLayout searchBar = findViewById(R.id.searchBar);


        // Initialize SQLite database helper
        dbHelper = new DatabaseHelper(this);

        // Retrieve and store username
        retrieveAndStoreUsername();

        imageViewLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle search bar visibility
                if (searchBar.getVisibility() == View.VISIBLE) {
                    searchBar.setVisibility(View.GONE);
                } else {
                    searchBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void retrieveAndStoreUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Retrieve username from Firebase (replace with your actual logic)
            String username = "Sample User"; // Replace with actual username retrieval

            // Check if username exists in SQLite first
            String storedUsername = getUsernameFromSQLite(userId);
            if (storedUsername == null) {
                // Username not found in SQLite, store it
                storeUsernameInSQLite(userId, username);
            } else {
                // Username found in SQLite, use it
                username = storedUsername;
            }

            textViewWelcome.setText("Bienvenido a XpresaT y " + username);
        }
    }

    private String getUsernameFromSQLite(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = null;
        Cursor cursor = db.query("user_info", new String[]{"username"}, "uid = ?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return username;
    }

    private void storeUsernameInSQLite(String userId, String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", userId);
        values.put("username", username);
        db.insert("user_info", null, values);
        db.close();
    }
}
