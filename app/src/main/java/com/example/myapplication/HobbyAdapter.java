package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class HobbyAdapter extends CursorAdapter {

    public HobbyAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_hobby, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNombre = view.findViewById(R.id.tvNombre);
        ImageView ivImagen = view.findViewById(R.id.ivImagen);

        @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
        @SuppressLint("Range") String imagen = cursor.getString(cursor.getColumnIndex("imagen"));

        tvNombre.setText(nombre);
        // Cargar la imagen desde el almacenamiento local
        // ivImagen.setImageURI(Uri.parse(imagen));
    }
}