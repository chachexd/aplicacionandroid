package com.example.myapplication;

public class Hobbie {
    private int id;
    private String nombre;
    private String foto;

    public Hobbie(int id, String nombre, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }
}
