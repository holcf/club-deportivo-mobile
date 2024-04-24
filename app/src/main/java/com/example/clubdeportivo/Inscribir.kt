package com.example.clubdeportivo

import android.app.DatePickerDialog
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

    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase

    // función para mostrar una alerta con un mensaje


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

        //radio button socio por defecto
        val radioButtonToCheck: RadioButton = findViewById(R.id.rbtSocio)
        radioButtonToCheck.isChecked = true

        //botón inscribir
        val btnInscribir: Button =findViewById(R.id.btnInscribir)
        btnInscribir.setOnClickListener {

            // inicializa la base de datos
            dbHelper = MiBaseDeDatosHelper(this)
            db = dbHelper.writableDatabase

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

            //verifica si el usuario ya existe
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${tablaTipoUsuario} WHERE DNI=${dni}", null)
            var usuarioExiste = false
            if (cursor.moveToNext() && cursor.getInt(0) > 0) {
                usuarioExiste = true
            }
            cursor.close()

            //verifica si faltan datos
            val faltanDatos = nombre.isEmpty() || dni == 0 || correo.isEmpty() || fechaInscripcion.isEmpty()

            if (usuarioExiste){
                mostrarAlerta(this, "Inscripción",
                    "El usuario ya existe.")
            } else if (faltanDatos) {
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
            else
             {
               db.execSQL(
                    "INSERT INTO $tablaTipoUsuario (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) " +
                            "VALUES (?, ?,?,?,?,?)",
                    arrayOf(null, nombre, dni, correo, fechaInscripcion, aptoFisico)
                )
               mostrarAlerta(
                    this,
                    "Inscripción",
                    "Inscripción realizada correctamente."
                )
            }
            db.close()
        }
    }

    private fun showDatePickerDialog() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                // Maneja la fecha seleccionada
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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