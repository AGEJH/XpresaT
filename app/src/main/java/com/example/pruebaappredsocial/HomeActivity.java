package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView textViewWelcome;
    private ImageView imageViewSelected;
    private int likeCount = 0;
    private int commentCount = 0;
    private int shareCount = 0;
    private DatabaseHelper dbHelper; // Instance of your SQLiteOpenHelper class
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved theme before setting the content view
        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_home);

        ImageButton buttonLike = findViewById(R.id.buttonLike);
        ImageButton buttonComment = findViewById(R.id.buttonComment);
        ImageButton buttonShare = findViewById(R.id.buttonShare);
        ImageButton buttonNotifications = findViewById(R.id.buttonNotifications);
        TextView textViewLikes = findViewById(R.id.textLikeCount); // Reference to TextView for likes
        TextView textViewComments = findViewById(R.id.textCommentCount); // Reference to TextView for comments
        TextView textViewShares = findViewById(R.id.textShareCount); // Reference to TextView for shares
        textViewWelcome = findViewById(R.id.textViewWelcome);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        Button buttonChooseImage = findViewById(R.id.buttonChooseImage);
        EditText postInput = findViewById(R.id.postInput);
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Button buttonToggleTheme = findViewById(R.id.buttonToggleTheme);

        buttonChooseImage.setOnClickListener(v -> openFileChooser());

        // Set onClickListener for Like button
        buttonLike.setOnClickListener(v -> {
            likeCount++;
            textViewLikes.setText(likeCount + " Likes");
        });

        // Set onClickListener for Comment button
        buttonComment.setOnClickListener(v -> {
            commentCount++;
            textViewComments.setText(commentCount + " Comments");
        });

        // Set onClickListener for Share button
        buttonShare.setOnClickListener(v -> {
            shareCount++;
            textViewShares.setText(shareCount + " Shares");
        });
        // Set onClickListener for Notifications button
        buttonNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });

        // Toggle theme button logic
        buttonToggleTheme.setOnClickListener(v -> {
            boolean isCurrentlyDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
            if (isCurrentlyDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("isDarkMode", false);
                editor.apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("isDarkMode", true);
                editor.apply();
            }
        });

        // Retrieve and store username
        retrieveAndStoreUsername();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageViewSelected);
        }
    }

    private void retrieveAndStoreUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Acceder al nombre del usuario desde Firebase Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("usuarios").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("name");
                    textViewWelcome.setText("Bienvenido a XpresaT " + username);
                    storeUsernameInSQLite(userId, username); // Almacena el nombre en SQLite
                } else {
                    // El documento del usuario no existe
                    textViewWelcome.setText("Bienvenido a XpresaT (nombre de usuario no encontrado)");
                }
            }).addOnFailureListener(e -> {
                // Manejar cualquier error aquí
                Log.e("HomeActivity", "Error al obtener el nombre del usuario", e);
                // Mostrar un mensaje de error en un TextView o un Toast
                Toast.makeText(HomeActivity.this, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
            });
        }
    }

    /*private String getUsernameFromSQLite(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = null;
        Cursor cursor = db.query("user_info", new String[]{"username"}, "uid = ?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return username;
    }  */

    private void storeUsernameInSQLite(String userId, String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", userId);
        values.put("username", username);
        db.insert("user_info", null, values);
        db.close();
    }
}
