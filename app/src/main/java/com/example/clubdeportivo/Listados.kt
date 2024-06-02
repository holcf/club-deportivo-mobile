package com.example.clubdeportivo

import android.content.ContentValues.TAG
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Socio (
    val nroSocio: Int,
    val nombre: String,
    val dni: Int
)
class sociosAdapter(private val socios: List<Socio>) :
    RecyclerView.Adapter<sociosAdapter.socioViewHolder>() {

    class socioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val nroSocioTextView: TextView = itemView.findViewById(R.id.nroSocioTextView)
        val dniTextView: TextView = itemView.findViewById(R.id.dniTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): socioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_listado_socios, parent, false)
        return socioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: socioViewHolder, position: Int) {
        val socio = socios[position]
        holder.nombreTextView.text = socio.nombre
        holder.nroSocioTextView.text = socio.nroSocio.toString()
        holder.dniTextView.text = socio.dni.toString()
    }

    override fun getItemCount(): Int {
        return socios.size
    }
}
class Listados : AppCompatActivity() {
    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val socios = mutableListOf<Socio>()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val btnListadoSocios: Button = findViewById<Button>(R.id.btnListadoSocios)
        btnListadoSocios.setOnClickListener {
            dbHelper = MiBaseDeDatosHelper(this)
            db = dbHelper.writableDatabase

            val cursor = db.rawQuery("SELECT * FROM socio", null)
            //val tableLayout = findViewById<android.widget.TableLayout>(R.id.tableLayout)
            while (cursor.moveToNext()) {
                val nroSocio = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val dni = cursor.getInt(2)
                socios.add(Socio(nroSocio, nombre, dni))

                /*
                val tableRow = TableRow(this)
                tableRow.setPadding(10, 10, 10, 10)
                val nroSocioTextView = TextView(this)
                nroSocioTextView.setPadding(10, 10, 10, 10)
                nroSocioTextView.text = nroSocio.toString()

                val nombreTextView = TextView(this)
                nombreTextView.setPadding(10, 10, 10, 10)

                nombreTextView.text = nombre
                val dniTextView = TextView(this)
                dniTextView.setPadding(10, 10, 10, 10)

                dniTextView.text = dni.toString()

                // Agregar TextViews a la fila
                tableRow.addView(nroSocioTextView)
                tableRow.addView(nombreTextView)
                tableRow.addView(dniTextView)

                // Agregar la fila a la tabla
                tableLayout.addView(tableRow)

                 */
            }
            cursor.close()
            db.close()
            Log.d(TAG, socios.toString())
            // Crear un adaptador para RecyclerView
            val adapter = sociosAdapter(socios)

            // Asignar el adaptador al RecyclerView
            recyclerView.adapter = adapter
        }
    }


}