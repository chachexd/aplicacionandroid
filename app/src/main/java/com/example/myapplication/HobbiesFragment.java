package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;  // Para los logs
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.DBConexion;

import java.util.ArrayList;

public class HobbiesFragment extends Fragment {

    private RecyclerView recyclerViewHobbies;
    private Button btnAddHobby;
    private DBConexion dbConexion;
    private SQLiteDatabase db;
    private ControladorRecyclerView controladorRecyclerView;
    private int usuarioId;

    public HobbiesFragment(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbies, container, false);

        recyclerViewHobbies = view.findViewById(R.id.recyclerViewHobbies);
        btnAddHobby = view.findViewById(R.id.btnAddHobby);

        // Configurar RecyclerView
        recyclerViewHobbies.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbConexion = new DBConexion(getActivity());
        db = dbConexion.getReadableDatabase();

        // Cargar los hobbies del usuario
        cargarHobbies();

        // Acción al pulsar el botón "Añadir Hobby"
        btnAddHobby.setOnClickListener(v -> {
            AddHobbyDialogFragment addHobbyDialog = new AddHobbyDialogFragment();
            Bundle args = new Bundle();
            args.putInt("usuarioId", usuarioId);
            addHobbyDialog.setArguments(args);
            addHobbyDialog.show(getFragmentManager(), "AddHobbyDialog");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Volver a cargar los hobbies al reanudarse el fragmento
        cargarHobbies();
    }

    public void cargarHobbies() {
        // Obtener los hobbies del usuario usando la columna "imagen" en lugar de "foto"
        Cursor cursor = dbConexion.selectHobbiesDeUsuario(db, usuarioId);
        ArrayList<Hobbie> hobbiesList = new ArrayList<>();

        // Opcional: imprimir las columnas del cursor para depurar
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Log.d("HobbiesFragment", "Columna: " + columnName);
        }

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String imagen = cursor.getString(cursor.getColumnIndex("imagen"));
            hobbiesList.add(new Hobbie(nombre, imagen));
        }
        cursor.close();

        // Actualizar el adaptador del RecyclerView
        if (controladorRecyclerView == null) {
            controladorRecyclerView = new ControladorRecyclerView(hobbiesList);
            recyclerViewHobbies.setAdapter(controladorRecyclerView);
        } else {
            controladorRecyclerView.setHobbyList(hobbiesList);
            controladorRecyclerView.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        super.onDestroyView();
    }
}
