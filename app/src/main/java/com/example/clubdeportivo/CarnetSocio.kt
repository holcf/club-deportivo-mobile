package com.example.clubdeportivo

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CarnetSocio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.club_deportivo_carnet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val socioNombre = intent.getStringExtra("SocioNombre")
        val tipoSocio = intent.getStringExtra("tipoSocio")
        val userId = intent.getIntExtra("SocioDNI", -1)
        val SocioEmail = intent.getStringExtra("SocioEmail")
        val SocioFechaInscripcion = intent.getStringExtra("SocioFechaInscripcion")

        val txtTipo: TextView = findViewById(R.id.txtValueTipoUsuario)
        txtTipo.text = "$tipoSocio"
        val txtNombre: TextView = findViewById(R.id.txtValueNombre)
        txtNombre.text = "$socioNombre"
        val txtUsuario: TextView = findViewById(R.id.txtValueDNI)
        txtUsuario.text = "$userId"
        val txtEmail: TextView = findViewById(R.id.txtValueEmail)
        txtEmail.text = "$SocioEmail"
        val txtFecha: TextView = findViewById(R.id.txtValueFechaInscripcion)
        txtFecha.text = "$SocioFechaInscripcion"
    }
}