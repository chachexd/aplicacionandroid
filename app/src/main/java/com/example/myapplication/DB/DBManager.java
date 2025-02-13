package com.example.myapplication.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBConexion conexion;
    private SQLiteDatabase basedatos;

    public DBManager(Context context) {
        this.conexion = new DBConexion(context);
    }

    //Apertura de la base de datos
    public DBManager open() throws SQLException {
        basedatos = conexion.getWritableDatabase();
        return this;
    }

    //Cerrar la base de datos
    public void close() {
        this.conexion.close();
    }
}
