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

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

public class ControladorRecyclerView extends RecyclerView.Adapter<ControladorRecyclerView.ViewHolder> {

    private ArrayList<Hobbie> hobbyList;

    public ControladorRecyclerView(ArrayList<Hobbie> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public void setHobbyList(ArrayList<Hobbie> hobbyList) {
        this.hobbyList = hobbyList;
        notifyDataSetChanged(); // Notificar cambios en el adaptador
    }

    public void removeHobby(int position) {
        hobbyList.remove(position);
        notifyItemRemoved(position);
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

        String imagePath = hobbie.getFoto();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Context context = holder.imagen.getContext();
                if (imagePath != null && !imagePath.isEmpty() && imagePath.startsWith("content://")) {
                    Uri imageUri = Uri.parse(imagePath);
                    Glide.with(context)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(holder.imagen);
                } else {
                    holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
                }

            } catch (Exception e) {
                e.printStackTrace();
                holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
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
