package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.DB.DBConexion;
import com.example.myapplication.R;
import com.example.myapplication.ViewPagerAdapter;
import com.example.myapplication.HobbiesFragment;
import com.example.myapplication.ViajesFragment;
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

        // Obtener el ID del usuario actual (puedes pasarlo desde LoginActivity)
        usuarioId = obtenerIdDelUsuario(); // Método para obtener el ID del usuario

        // Configurar el ViewPager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Añadir fragments
        adapter.addFragment(new HobbiesFragment(usuarioId), "Hobbies"); // Pasar el ID del usuario
        adapter.addFragment(new ViajesFragment(), "Viajes");

        viewPager.setAdapter(adapter);
    }

    // Método de ejemplo para obtener el ID del usuario
    private int obtenerIdDelUsuario() {
        // Aquí debes implementar la lógica para obtener el ID del usuario actual
        // Por ejemplo, puedes pasarlo desde LoginActivity usando un Intent
        return getIntent().getIntExtra("USUARIO_ID", 0); // Recibe el ID del usuario como parámetro en el intento

        // Este ejemplo devuelve un ID de usuario fijo para probar
        // Puedes cambiarlo por la lógica adecuada para obtener el ID del usuario actual
        //return 1; // Este es un valor de ejemplo
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}