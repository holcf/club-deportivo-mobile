package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtUsuario: TextView = findViewById(R.id.txtUsuario)
        txtUsuario.text = "Usuario:  ${DatosCompartidos.usuarioLogueado}"


        val btnInscribir: Button =findViewById(R.id.btnInscribir)
        btnInscribir.setOnClickListener {
            val intentar = Intent(this, Inscribir::class.java)
            startActivity(intentar)
        }

        val btnCobrarCuota: Button =findViewById(R.id.btnCobrarCuota)
        btnCobrarCuota.setOnClickListener {
            val intentar = Intent(this, CobroCutas::class.java)
            startActivity(intentar)
        }


        val btnListados: Button =findViewById(R.id.btnListado)
        btnListados.setOnClickListener {
            val intentar = Intent(this, Listados::class.java)
            startActivity(intentar)
        }

    }


}