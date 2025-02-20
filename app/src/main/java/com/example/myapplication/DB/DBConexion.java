package com.example.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.Hobbie;
import com.example.myapplication.Usuario;

public class DBConexion extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DiegoCuestaDiazDB";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_USUARIOS = "usuarios";
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String TABLE_USUARIOS_HOBBIES = "usuarios_hobbies";

    public DBConexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_HOBBIES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, descripcion TEXT, imagen TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS_HOBBIES + " (usuario_id INTEGER, hobby_id INTEGER, PRIMARY KEY(usuario_id, hobby_id), FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE, FOREIGN KEY(hobby_id) REFERENCES hobbies(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_HOBBIES + " ADD COLUMN descripcion TEXT");
            } catch (Exception e) {
                Log.e("DBUpgrade", "Error al añadir la columna descripcion: " + e.getMessage());
            }
        }
    }

    public boolean insertarUsuario(SQLiteDatabase db, Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("usuario", usuario.getNombre());
        values.put("password", usuario.getPassword());

        long resultado = db.insertWithOnConflict(TABLE_USUARIOS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return resultado != -1;
    }

    public long insertarHobby(SQLiteDatabase db, Hobbie hobby) {
        ContentValues values = new ContentValues();
        values.put("nombre", hobby.getNombre());
        values.put("descripcion", hobby.getDescripcion()); // Asegurar que la descripción se almacene
        values.put("imagen", hobby.getFoto());

        long hobbyId = db.insert(TABLE_HOBBIES, null, values);
        if (hobbyId == -1) {
            Log.e("DBConexion", "Error al insertar el hobby en la base de datos.");
        }
        return hobbyId;
    }

    public boolean añadirHobbyAUsuario(SQLiteDatabase db, int usuarioId, long hobbyId) {
        ContentValues values = new ContentValues();
        values.put("usuario_id", usuarioId);
        values.put("hobby_id", hobbyId);

        long resultado = db.insert(TABLE_USUARIOS_HOBBIES, null, values);
        return resultado != -1;
    }

    public Cursor selectHobbiesDeUsuario(SQLiteDatabase db, int usuarioId) {
        return db.rawQuery("SELECT h.id, h.nombre, h.descripcion, h.imagen FROM " + TABLE_HOBBIES + " h " +
                "INNER JOIN " + TABLE_USUARIOS_HOBBIES + " uh ON h.id = uh.hobby_id " +
                "WHERE uh.usuario_id = ?", new String[]{String.valueOf(usuarioId)});
    }

    public boolean eliminarHobby(SQLiteDatabase db, int hobbyId) {
        db.delete(TABLE_USUARIOS_HOBBIES, "hobby_id = ?", new String[]{String.valueOf(hobbyId)});
        int filasEliminadas = db.delete(TABLE_HOBBIES, "id = ?", new String[]{String.valueOf(hobbyId)});
        return filasEliminadas > 0;
    }

    public String obtenerPasswordUsuario(SQLiteDatabase db, String usuario) {
        String password = null;
        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_USUARIOS + " WHERE usuario = ?", new String[]{usuario});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                password = cursor.getString(0);
            }
            cursor.close();
        }
        return password;
    }

    public int obtenerIdUsuario(SQLiteDatabase db, String usuario) {
        int id = -1;
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USUARIOS + " WHERE usuario = ?", new String[]{usuario});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
            cursor.close();
        }
        return id;
    }
}
