package com.example.myapplication;

public class Hobbie {
    private int id;
    private String nombre;
    private String descripcion; // Nuevo campo
    private String foto;

    public Hobbie(int id, String nombre, String descripcion, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; } // Nuevo getter
    public String getFoto() { return foto; }
}
