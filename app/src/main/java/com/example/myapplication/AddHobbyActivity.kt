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
    private lateinit var ivImagen: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var dbConexion: DBConexion
    private lateinit var db: SQLiteDatabase
    private var usuarioId: Int = -1
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            ivImagen.setImageURI(uri) // Mostrar la imagen seleccionada
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hobby)

        etNombre = findViewById(R.id.etNombre)
        ivImagen = findViewById(R.id.ivImagen)
        btnGuardar = findViewById(R.id.btnGuardar)

        dbConexion = DBConexion(this)
        db = dbConexion.writableDatabase

        usuarioId = intent.getIntExtra("usuarioId", -1)

        ivImagen.setOnClickListener {
            pickImageLauncher.launch("image/*") // Abrir la galería
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isNotEmpty()) {
                val imagenString = imageUri?.toString() ?: "" // Convertir la URI a String

                val nuevoHobby = Hobbie(0, nombre, imagenString) // Guardar la URI en la base de datos
                val hobbyId = dbConexion.insertarHobby(db, nuevoHobby)

                if (hobbyId != -1L) {
                    val resultado = dbConexion.añadirHobbyAUsuario(db, usuarioId,
                        hobbyId.toInt().toLong()
                    )
                    if (resultado) {
                        Toast.makeText(this, "Hobby añadido correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al asociar hobby con usuario", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al añadir hobby", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingresa un nombre para el hobby", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
