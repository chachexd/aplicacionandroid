package com.example.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Hobby;
import com.example.myapplication.Usuario;

public class DBConexion extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "app_db";
    public static final int DATABASE_VERSION = 1;

    // Tablas y columnas
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_HOBBYS = "hobbies";
    public static final String TABLE_USUARIO_HOBBIES = "usuario_hobbies"; // Nueva tabla intermedia

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_CONTRASENA = "contrasena";
    public static final String COLUMN_NOMBRE_HOBBY = "nombre";
    public static final String COLUMN_IMAGEN_HOBBY = "imagen";
    public static final String COLUMN_USUARIO_ID = "usuario_id";
    public static final String COLUMN_HOBBY_ID = "hobby_id";

    public DBConexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USUARIO + " TEXT, "
                + COLUMN_CONTRASENA + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_USUARIOS);

        // Crear tabla de hobbies
        String CREATE_TABLE_HOBBYS = "CREATE TABLE " + TABLE_HOBBYS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOMBRE_HOBBY + " TEXT, "
                + COLUMN_IMAGEN_HOBBY + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_HOBBYS);

        // Crear tabla intermedia usuario_hobbies
        String CREATE_TABLE_USUARIO_HOBBIES = "CREATE TABLE " + TABLE_USUARIO_HOBBIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USUARIO_ID + " INTEGER, "
                + COLUMN_HOBBY_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID + "), "
                + "FOREIGN KEY(" + COLUMN_HOBBY_ID + ") REFERENCES " + TABLE_HOBBYS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_USUARIO_HOBBIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO_HOBBIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBYS);
        onCreate(db);
    }

    // Insertar usuario
    public void insertarUsuario(SQLiteDatabase db, Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO, usuario.getNombre());
        values.put(COLUMN_CONTRASENA, usuario.getContrasena());
        db.insert(TABLE_USUARIOS, null, values);
    }

    // Verificar usuario
    public boolean verificarUsuario(SQLiteDatabase db, Usuario usuario) {
        Cursor cursor = db.query(TABLE_USUARIOS, new String[]{COLUMN_ID, COLUMN_USUARIO, COLUMN_CONTRASENA},
                COLUMN_USUARIO + "=? AND " + COLUMN_CONTRASENA + "=?", new String[]{usuario.getNombre(), usuario.getContrasena()},
                null, null, null);

        boolean isValid = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }

    // Insertar hobby
    public long insertarHobby(SQLiteDatabase db, Hobby hobby) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_HOBBY, hobby.getNombre());
        values.put(COLUMN_IMAGEN_HOBBY, hobby.getImagen());
        return db.insert(TABLE_HOBBYS, null, values);
    }

    // Asociar un hobby a un usuario
    public void añadirHobbyAUsuario(SQLiteDatabase db, int usuarioId, int hobbyId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_ID, usuarioId);
        values.put(COLUMN_HOBBY_ID, hobbyId);
        db.insert(TABLE_USUARIO_HOBBIES, null, values);
    }

    // Obtener todos los hobbies
    public Cursor selectHobbies(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + TABLE_HOBBYS, null);
    }

    // Obtener hobbies de un usuario
    public Cursor selectHobbiesDeUsuario(SQLiteDatabase db, int usuarioId) {
        return db.rawQuery(
                "SELECT h.id AS _id, h.nombre, h.imagen FROM " + TABLE_HOBBYS + " h " +
                        "INNER JOIN " + TABLE_USUARIO_HOBBIES + " uh ON h." + COLUMN_ID + " = uh." + COLUMN_HOBBY_ID +
                        " WHERE uh." + COLUMN_USUARIO_ID + " = ?",
                new String[]{String.valueOf(usuarioId)}
        );
    }
}