package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.DB.DBConexion;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DBConexion dbConexion;
    private SQLiteDatabase db;
    private int usuarioId; // ID del usuario que ha iniciado sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Inicializar la base de datos
        dbConexion = new DBConexion(this);
        db = dbConexion.getWritableDatabase();

        // Obtener el ID del usuario actual
        usuarioId = getIntent().getIntExtra("USUARIO_ID", 0);

        usuarioId = getIntent().getIntExtra("USUARIO_ID", -1);
        if (usuarioId == -1) {
            Toast.makeText(this, "Error al obtener usuario. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }


        // Configurar el ViewPager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Añadir fragments solo si el usuario es válido
        adapter.addFragment(new HobbiesFragment(usuarioId), "Hobbies");
        adapter.addFragment(new ViajesFragment(), "Viajes");

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        super.onDestroy();
    }
}
