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
                new Thread(() -> validarLogin()).start(); // Mueve la validación a otro hilo
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> registrarUsuario()).start(); // Mueve el registro a otro hilo
            }
        });
    }

    private void registrarUsuario() {
        String usuario = etUsuario.getText().toString();
        String password = etContrasena.getText().toString();

        if (!usuario.isEmpty() && !password.isEmpty()) {
            String passwordEncriptada = Seguridad.encriptarMD5(password);
            Usuario nuevoUsuario = new Usuario(usuario, passwordEncriptada);
            boolean registrado = dbConexion.insertarUsuario(db, nuevoUsuario);

            runOnUiThread(() -> {
                if (registrado) {
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(() -> Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show());
        }
    }

    private void validarLogin() {
        String usuario = etUsuario.getText().toString();
        String passwordIngresada = etContrasena.getText().toString();

        if (!usuario.isEmpty() && !passwordIngresada.isEmpty()) {
            String passwordEncriptada = Seguridad.encriptarMD5(passwordIngresada);
            String passwordGuardada = dbConexion.obtenerPasswordUsuario(db, usuario);

            if (passwordEncriptada.equals(passwordGuardada)) {
                int usuarioId = dbConexion.obtenerIdUsuario(db, usuario);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USUARIO_ID", usuarioId);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show());
            }
        } else {
            runOnUiThread(() -> Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show());
        }
    }
}
