package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DB.DBConexion

class AddHobbyActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var ivImagen: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var dbConexion: DBConexion
    private lateinit var db: SQLiteDatabase
    private var usuarioId: Int = -1
    private var imageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            ivImagen.setImageURI(it) // Muestra la imagen seleccionada
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobby)

        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        ivImagen = findViewById(R.id.ivImagen)
        btnGuardar = findViewById(R.id.btnGuardar)

        dbConexion = DBConexion(this)
        db = dbConexion.writableDatabase

        usuarioId = intent.getIntExtra("usuarioId", -1)

        // Seleccionar imagen al hacer clic en la imagen
        ivImagen.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val imagenString = imageUri?.toString() ?: ""

            if (nombre.isNotEmpty()) {
                val nuevoHobby = Hobbie(0, nombre, descripcion, imagenString)
                val hobbyId = dbConexion.insertarHobby(db, nuevoHobby) // Inserta el hobby y obtiene su ID

                if (hobbyId != -1L) {
                    val agregado = dbConexion.añadirHobbyAUsuario(db, usuarioId, hobbyId) // Vincula el hobby al usuario

                    if (agregado) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al vincular el hobby con el usuario", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al guardar el hobby", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingresa un nombre para el hobby", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
