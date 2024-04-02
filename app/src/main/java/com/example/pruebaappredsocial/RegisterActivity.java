package com.example.pruebaappredsocial;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContraseña, editTextRepetirContraseña;
    private Button buttonRegistrarse;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombre = findViewById(R.id.tnombre);
        editTextApellido = findViewById(R.id.tapellido);
        editTextCorreo = findViewById(R.id.tcorreo);
        editTextContraseña = findViewById(R.id.tcontraseña);
        editTextRepetirContraseña = findViewById(R.id.tcontraseña2);

        dbHelper = new DatabaseHelper(this);

        // Maneja el evento de clic en el botón de registro
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        // Obtiene los datos del formulario
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        // Abre la base de datos en modo escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un nuevo registro de usuario
        ContentValues values = new ContentValues();
        values.put("name", nombre);
        values.put("lastname", apellido);
        values.put("email", correo);
        values.put("password", contraseña);

        // Inserta el registro en la tabla 'user'
        long newRowId = db.insert("user", null, values);

        // Cierra la conexión de la base de datos
        dbHelper.close();

        // Lógica adicional después del registro...
    }
}
