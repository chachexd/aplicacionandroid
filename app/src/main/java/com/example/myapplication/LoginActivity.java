package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DB.DBConexion;
import com.example.myapplication.R;
import com.example.myapplication.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btnLogin, btnRegistrar;
    private DBConexion dbConexion;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        dbConexion = new DBConexion(this);
        db = dbConexion.getWritableDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString();
                String contrasena = etContrasena.getText().toString();

                Usuario user = new Usuario(usuario, contrasena);
                if (dbConexion.verificarUsuario(db, user)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString();
                String contrasena = etContrasena.getText().toString();

                Usuario user = new Usuario(usuario, contrasena);
                dbConexion.insertarUsuario(db, user);
                Toast.makeText(LoginActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}