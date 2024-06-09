package com.example.clubdeportivo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.RadioButton

import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ComprobantePago : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprobante_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //radio button rbtEfectivo por defecto
        val radioButtonEfectivo: RadioButton = findViewById(R.id.rbtEfectivo)
        val radioButtonTarjeta: RadioButton = findViewById(R.id.rbtTarjeta)

        if (DatosCompartidos.cuotaPagada?.metodoPago == MetodoPago.EFECTIVO.value) {
            radioButtonEfectivo.isChecked = true
        } else {
            radioButtonTarjeta.isChecked = true
        }

        findViewById<TextView>(R.id.editTextFechaPago).text = DatosCompartidos.cuotaPagada?.fechaPago
        findViewById<TextView>(R.id.editTextMonto).text = DatosCompartidos.cuotaPagada?.monto.toString()
        findViewById<TextView>(R.id.textViewDNI).text =
            "${DatosCompartidos.socioPago?.dni.toString()} - ${DatosCompartidos.socioPago?.nombre}"

        findViewById<TextView>(R.id.textTipoUsuario).text = DatosCompartidos.tipoSocioPago
        findViewById<TextView>(R.id.textTipoComprobantePago).text = DatosCompartidos.tipoPago

        if (DatosCompartidos.tipoPago == "Actividad") {
            // hide cuota fields
            findViewById<TextView>(R.id.CuotaInicio).visibility = TextView.GONE
            findViewById<TextView>(R.id.CuotaFin).visibility = TextView.GONE
            findViewById<TextView>(R.id.editTextInicioCuota).visibility = TextView.GONE
            findViewById<TextView>(R.id.editTextFinCuota).visibility = TextView.GONE
        } else {
            findViewById<TextView>(R.id.editTextInicioCuota).text = DatosCompartidos.cuotaPagada?.fechaInicio
            findViewById<TextView>(R.id.editTextFinCuota).text = DatosCompartidos.cuotaPagada?.vencimiento
        }
    }
}