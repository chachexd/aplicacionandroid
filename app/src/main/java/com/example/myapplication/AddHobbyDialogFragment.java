package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.DB.DBConexion;
import com.example.myapplication.Hobbie;
import com.example.myapplication.R;

public class AddHobbyDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;  // Código para el selector de imágenes

    private EditText etNombre;
    private ImageView ivImagen;
    private DBConexion dbConexion;
    private SQLiteDatabase db;
    private Uri imageUri;  // URI de la imagen seleccionada
    private int usuarioId; // ID del usuario que está añadiendo el hobby

    @SuppressLint("DialogFragmentCallbacksDetector")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_hobby, null);

        etNombre = view.findViewById(R.id.etNombre);
        ivImagen = view.findViewById(R.id.ivImagen);

        dbConexion = new DBConexion(getActivity());
        db = dbConexion.getWritableDatabase();

        // Obtener el usuarioId pasado desde el fragmento
        if (getArguments() != null) {
            usuarioId = getArguments().getInt("usuarioId");
        }

        // Configurar el botón de añadir imagen
        ivImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir el selector de imágenes
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        builder.setView(view)
                .setTitle("Añadir Hobby")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = etNombre.getText().toString();
                        String imagen = imageUri != null ? imageUri.toString() : ""; // Convertir la URI a string
                        Hobbie hobby = new Hobbie(nombre, imagen);

                        // Insertar hobby en la tabla hobbies
                        long hobbyId = dbConexion.insertarHobby(db, hobby);

                        // Asociar el hobby al usuario en la tabla usuario_hobbies
                        dbConexion.añadirHobbyAUsuario(db, usuarioId, (int) hobbyId);

                        // Recargar los hobbies al añadir uno nuevo
                        HobbiesFragment hobbiesFragment = (HobbiesFragment) getParentFragment();
                        if (hobbiesFragment != null) {
                            hobbiesFragment.cargarHobbies(); // Llamar a la función para actualizar la lista
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // Recargar los hobbies al cerrar el diálogo
                        HobbiesFragment hobbiesFragment = (HobbiesFragment) getParentFragment();
                        if (hobbiesFragment != null) {
                            hobbiesFragment.cargarHobbies(); // Llamar a la función para actualizar la lista
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            // Obtener la URI de la imagen seleccionada
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                ivImagen.setImageURI(imageUri);  // Mostrar la imagen seleccionada en el ImageView
            }
        }
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
