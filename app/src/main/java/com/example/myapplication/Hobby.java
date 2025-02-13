package com.example.myapplication;

public class Hobby {
    private String nombre;
    private String imagen; // Puede ser una URI o una ruta de archivo

    // Constructor
    public Hobby(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}