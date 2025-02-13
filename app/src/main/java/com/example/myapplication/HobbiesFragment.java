package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.DB.DBConexion;
import com.example.myapplication.R;
import com.example.myapplication.HobbyAdapter;

public class HobbiesFragment extends Fragment {

    private ListView listViewHobbies;
    private Button btnAddHobby;
    private DBConexion dbConexion;
    private SQLiteDatabase db;
    private HobbyAdapter hobbyAdapter;
    private int usuarioId; // ID del usuario que ha iniciado sesión

    public HobbiesFragment(int usuarioId) {
        this.usuarioId = usuarioId; // Recibir el ID del usuario
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hobbies, container, false);

        listViewHobbies = view.findViewById(R.id.listViewHobbies);
        btnAddHobby = view.findViewById(R.id.btnAddHobby);

        dbConexion = new DBConexion(getActivity());
        db = dbConexion.getReadableDatabase();

        // Obtener solo los hobbies del usuario actual
        Cursor cursor = dbConexion.selectHobbiesDeUsuario(db, usuarioId);
        hobbyAdapter = new HobbyAdapter(getActivity(), cursor);
        listViewHobbies.setAdapter(hobbyAdapter);

        // Acción al pulsar el botón "Añadir Hobby"
        btnAddHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un nuevo fragmento de diálogo para añadir hobby
                AddHobbyDialogFragment addHobbyDialog = new AddHobbyDialogFragment();

                // Pasar el usuarioId al diálogo
                Bundle args = new Bundle();
                args.putInt("usuarioId", usuarioId); // pasamos el usuarioId
                addHobbyDialog.setArguments(args);

                // Mostrar el diálogo
                addHobbyDialog.show(getFragmentManager(), "AddHobbyDialog");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();
    }
}
