package com.example.myapplication;

public class Hobbie {

    private String nombre;
    private String foto;

    // Constructor
    public Hobbie(String nombre, String foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
