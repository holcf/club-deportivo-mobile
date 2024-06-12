package com.example.clubdeportivo

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
 import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.metodos.mostrarAlerta
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Inscribir : AppCompatActivity() {
    private lateinit var editTextDate: EditText
    private lateinit var calendar: Calendar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //configura textview de la fecha para que muestre el calendario al hacer click
        editTextDate = findViewById(R.id.editTextDate)
        calendar = Calendar.getInstance()
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        //desactivar botón ver carnet hasta que se inscriba un usuario
        val btnVerCarnet: Button = findViewById(R.id.btnVerCarnet)
        btnVerCarnet.isEnabled = false
        btnVerCarnet.setOnClickListener {
            val radioGroup: RadioGroup = findViewById(R.id.radiogroup)
            val selectedRadioButtonText: String = (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()
            val intentar = Intent(this, CarnetSocio::class.java).apply {
                putExtra("SocioNombre", findViewById<EditText>(R.id.editTextNombre).text.toString())
                putExtra("tipoSocio", selectedRadioButtonText)
                putExtra("SocioDNI", findViewById<EditText>(R.id.editTextDNI).text.toString().toInt())
                putExtra("SocioEmail", findViewById<EditText>(R.id.editTextEmail).text.toString())
                putExtra("SocioFechaInscripcion", editTextDate.text.toString())
            }
            startActivity(intentar)
        }

        //radio button socio por defecto
        val radioButtonToCheck: RadioButton = findViewById(R.id.rbtSocio)
        radioButtonToCheck.isChecked = true

        //botón inscribir
        val btnInscribir: Button =findViewById(R.id.btnInscribir)
        btnInscribir.setOnClickListener {


            // define con que tabla trabajar (socio o nosocio) según la opción seleccionada
            val radioGroup: RadioGroup = findViewById(R.id.radiogroup)
            val selectedRadioButtonText: String = (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()

            var tablaTipoUsuario = "socio"
            if (selectedRadioButtonText == "No Socio") {
                tablaTipoUsuario = "nosocio"
            }

            //obtiene los valores del formulario
            val nombre: String = findViewById<EditText>(R.id.editTextNombre).text.toString()
            var dni: Number = 0
            if (!findViewById<EditText>(R.id.editTextDNI).text?.toString().isNullOrEmpty()) {
                dni = findViewById<EditText>(R.id.editTextDNI).text.toString().toInt()
            }
            val correo: String = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val fechaInscripcion: String = editTextDate.text.toString()
            var aptoFisico: Number = 0
            if (findViewById<CheckBox>(R.id.chkAptoFisico).isChecked) {
                aptoFisico = 1
            }

            //verifica si faltan datos
            val faltanDatos = nombre.isEmpty() || dni == 0 || correo.isEmpty() || fechaInscripcion.isEmpty()

            if (faltanDatos) {
                if (dni == 0) {
                    mostrarAlerta(this, "Inscripción",
                        "El DNI debe ser un número mayor a cero.")
                } else {
                    mostrarAlerta(
                        this, "Inscripción",
                        "Falta completar algún campo del formulario."
                    )
                }
            }
            else {
                mostrarAlerta(
                    this,
                    "Inscripción",
                    "Inscripción realizada correctamente."
                )
                btnVerCarnet.isEnabled = true
                btnInscribir.isEnabled = false
            }
        }

        val btnNuevaInscripcion: Button = findViewById(R.id.btnNuevaInscripcion)
        btnNuevaInscripcion.setOnClickListener {
            // Limpia los campos del formulario
            findViewById<EditText>(R.id.editTextNombre).setText("")
            findViewById<EditText>(R.id.editTextDNI).setText("")
            findViewById<EditText>(R.id.editTextEmail).setText("")
            editTextDate.setText("")
            findViewById<CheckBox>(R.id.chkAptoFisico).isChecked = false
            btnVerCarnet.isEnabled = false
            btnInscribir.isEnabled = true
        }
    }

    private fun showDatePickerDialog() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Maneja la fecha seleccionada
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)

                editTextDate.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }
}