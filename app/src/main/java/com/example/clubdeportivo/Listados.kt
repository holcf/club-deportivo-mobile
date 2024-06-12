package com.example.clubdeportivo


/*
 SELECT Nsocio, Nombre, DNI, ifnull (MCuota, '') AS VencimientoCuota
FROM (
  SELECT socio.Nsocio, socio.Nombre, socio.DNI, MAX(cuotaSocio.Vencimiento) AS MCuota
  FROM socio
  JOIN cuotaSocio ON socio.Nsocio = cuotaSocio.Nsocio
  GROUP BY socio.Nsocio
) t1
WHERE datetime(MCuota) <= datetime('now');
 */


import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    val dni: Int,
)

data class SocioListado (
    val nroSocio: Int,
    val nombre: String,
    val dni: Int,
    val vencimientoCuota: String
)
class SociosAdapter(private val socios: List<SocioListado>) :
    RecyclerView.Adapter<SociosAdapter.SocioViewHolder>() {

    class SocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val nroSocioTextView: TextView = itemView.findViewById(R.id.nroSocioTextView)
        val dniTextView: TextView = itemView.findViewById(R.id.dniTextView)
        val vencimientoCuotaTextView: TextView = itemView.findViewById(R.id.vencimientoTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_listado_socios, parent, false)
        return SocioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = socios[position]
        holder.nombreTextView.text = socio.nombre
        holder.nroSocioTextView.text = socio.nroSocio.toString()
        holder.dniTextView.text = socio.dni.toString()
        holder.vencimientoCuotaTextView.text = socio.vencimientoCuota
    }

    override fun getItemCount(): Int {
        return socios.size
    }
}
class Listados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val socios = mutableListOf<SocioListado>()
        socios.add(SocioListado(741, "Florencia Gómez", 12345678, "11/5"))
        socios.add(SocioListado(852, "Octavio Giménez", 98765432, "15/5"))
        var selectedNoSocio: Socio? = null

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Crear un adaptador para RecyclerView
        val adapter = SociosAdapter(socios)

        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = adapter

    }


}