package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.InputStream;
import java.util.ArrayList;

public class ControladorRecyclerView extends RecyclerView.Adapter<ControladorRecyclerView.ViewHolder> {

    private ArrayList<Hobbie> hobbyList;

    public ControladorRecyclerView(ArrayList<Hobbie> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public void setHobbyList(ArrayList<Hobbie> hobbyList) {
        this.hobbyList = hobbyList;
        notifyDataSetChanged();
    }

    public void addHobbie(Hobbie hobbie) {
        hobbyList.add(hobbie);
        notifyItemInserted(hobbyList.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hobbie hobbie = hobbyList.get(position);
        holder.nombre.setText(hobbie.getNombre());

        // Cargar la imagen desde el URI de contenido
        String imagePath = hobbie.getFoto(); // Se asume que getFoto() devuelve un URI (String)
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Context context = holder.imagen.getContext();
                Uri imageUri = Uri.parse(imagePath);
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.imagen.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                // En caso de error, puedes poner una imagen por defecto
                holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            // Si no hay ruta de imagen, se asigna una imagen por defecto
            holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return hobbyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        ImageView imagen;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.hobby_nombre);
            imagen = itemView.findViewById(R.id.hobby_imagen);
        }
    }
}