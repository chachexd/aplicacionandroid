package com.example.myapplication;

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
import com.example.myapplication.Hobby;
import com.example.myapplication.R;

public class AddHobbyDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;  // Código para el selector de imágenes

    private EditText etNombre;
    private ImageView ivImagen;
    private DBConexion dbConexion;
    private SQLiteDatabase db;
    private Uri imageUri;  // URI de la imagen seleccionada

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_hobby, null);

        etNombre = view.findViewById(R.id.etNombre);
        ivImagen = view.findViewById(R.id.ivImagen);

        dbConexion = new DBConexion(getActivity());
        db = dbConexion.getWritableDatabase();

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
                        Hobby hobby = new Hobby(nombre, imagen);
                        dbConexion.insertarHobby(db, hobby);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
