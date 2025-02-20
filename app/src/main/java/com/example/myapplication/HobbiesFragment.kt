package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DB.DBConexion

class HobbiesFragment(private val usuarioId: Int) : Fragment() {

    private lateinit var recyclerViewHobbies: RecyclerView
    private lateinit var btnAddHobby: Button
    private lateinit var dbConexion: DBConexion
    private lateinit var db: SQLiteDatabase
    private lateinit var controladorRecyclerView: ControladorRecyclerView
    private var hobbiesList = ArrayList<Hobbie>()

    private val addHobbyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            cargarHobbies() // Asegura que la lista se actualiza solo si se agregó correctamente
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_hobbies, container, false)

        recyclerViewHobbies = view.findViewById(R.id.recyclerViewHobbies)
        btnAddHobby = view.findViewById(R.id.btnAddHobby)

        recyclerViewHobbies.layoutManager = LinearLayoutManager(activity)
        dbConexion = DBConexion(requireActivity())
        db = dbConexion.writableDatabase

        configurarSwipeParaEliminar()
        cargarHobbies()

        btnAddHobby.setOnClickListener {
            val intent = Intent(activity, AddHobbyActivity::class.java)
            intent.putExtra("usuarioId", usuarioId)
            addHobbyLauncher.launch(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarHobbies()
    }

    private fun cargarHobbies() {
        hobbiesList.clear()
        val cursor = dbConexion.selectHobbiesDeUsuario(db, usuarioId)

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val nombre = it.getString(it.getColumnIndexOrThrow("nombre"))
                val descripcion = it.getString(it.getColumnIndexOrThrow("descripcion")) ?: "" // Asegurar que no sea null
                val imagen = it.getString(it.getColumnIndexOrThrow("imagen")) ?: ""

                // Validar que la URI es válida
                if (imagen.isNotEmpty() && !imagen.startsWith("content://")) {
                    Log.e("HobbiesFragment", "URI inválida detectada: $imagen")
                }

                // Ahora se incluyen todos los parámetros en el objeto Hobbie
                hobbiesList.add(Hobbie(id, nombre, descripcion, imagen))
            }
        }

        actualizarRecyclerView()
    }

    private fun actualizarRecyclerView() {
        if (!::controladorRecyclerView.isInitialized) {
            controladorRecyclerView = ControladorRecyclerView(hobbiesList)
            recyclerViewHobbies.adapter = controladorRecyclerView
        } else {
            controladorRecyclerView.setHobbyList(hobbiesList)
            controladorRecyclerView.notifyDataSetChanged()
        }
    }

    private fun configurarSwipeParaEliminar() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position < 0 || position >= hobbiesList.size) {
                    return
                }

                val hobby = hobbiesList[position]

                // Validar que el hobby tiene un ID antes de intentar eliminarlo
                if (hobby.id != -1) {
                    val eliminado = dbConexion.eliminarHobby(db, hobby.id)
                    if (eliminado) {
                        hobbiesList.removeAt(position)
                        controladorRecyclerView.notifyItemRemoved(position)
                    } else {
                        controladorRecyclerView.notifyItemChanged(position) // Restaurar en caso de fallo
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val paint = Paint()
                paint.color = Color.RED

                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.left.toFloat() + dX,
                    itemView.bottom.toFloat(),
                    paint
                )

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewHobbies)
    }
}
