package com.example.clubdeportivo

import android.app.DatePickerDialog
import android.content.ContentValues
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
    private lateinit var btnVerCarnet: Button
    private lateinit var btnInscribir: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonToCheck: RadioButton
    private lateinit var editTextNombre: EditText
    private lateinit var editTextDNI: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var chkAptoFisico: CheckBox

    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnInscribir = findViewById(R.id.btnInscribir)
        btnVerCarnet = findViewById(R.id.btnVerCarnet)
        radioGroup = findViewById(R.id.radiogroup)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextDNI = findViewById(R.id.editTextDNI)
        editTextEmail = findViewById(R.id.editTextEmail)
        chkAptoFisico = findViewById(R.id.chkAptoFisico)

        //configura textview de la fecha para que muestre el calendario al hacer click
        editTextDate = findViewById(R.id.editTextDate)
        calendar = Calendar.getInstance()
        editTextDate.setOnClickListener {
            mostrarCalendario()
        }

        //desactivar botón ver carnet hasta que se inscriba un usuario
        btnVerCarnet.isEnabled = false

        //agrega listener al boton Ver Carnet para que muestre el carnet al hacer click
        btnVerCarnet.setOnClickListener {
            showCarnet()
        }

        //setear radio button a socio por defecto
        radioButtonToCheck = findViewById(R.id.rbtSocio)
        radioButtonToCheck.isChecked = true

        //agrega listener al boton Ver Carnet para que intente inscribir el usuario al hacer click
        btnInscribir.setOnClickListener {
            inscribir()
        }

        val btnNuevaInscripcion: Button = findViewById(R.id.btnNuevaInscripcion)
        btnNuevaInscripcion.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun mostrarCalendario() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)

        editTextDate.setText(selectedDate)
    }

    private fun showCarnet() {
        val selectedRadioButtonText: String = (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()
        val intentar = Intent(this, CarnetSocio::class.java).apply {
            putExtra("SocioNombre", editTextNombre.text.toString())
            putExtra("tipoSocio", selectedRadioButtonText)
            putExtra("SocioDNI", editTextDNI.text.toString().toInt())
            putExtra("SocioEmail", editTextEmail.text.toString())
            putExtra("SocioFechaInscripcion", editTextDate.text.toString())
        }
        startActivity(intentar)
    }

    private fun limpiarCampos() {
        disableEnable(true)
        editTextNombre.setText("")
        editTextDNI.setText("")
        editTextEmail.setText("")
        editTextDate.setText("")
        chkAptoFisico.isChecked = false
        btnVerCarnet.isEnabled = false
        btnInscribir.isEnabled = true
    }

    private fun inscribir() {
        // inicializa la base de datos
        dbHelper = MiBaseDeDatosHelper(this)
        db = dbHelper.writableDatabase

        // define con que tabla trabajar (socio o nosocio) según la opción seleccionada
        radioGroup = findViewById(R.id.radiogroup)
        val selectedRadioButtonText: String = (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()

        var tablaTipoUsuario = "socio"
        if (selectedRadioButtonText == "No Socio") {
            tablaTipoUsuario = "nosocio"
        }

        //obtiene los valores del formulario
        val nombre: String = editTextNombre.text.toString()
        var dni: Number = 0
        if (!editTextDNI.text?.toString().isNullOrEmpty()) {
            dni = editTextDNI.text.toString().toInt()
        }
        val correo: String = editTextEmail.text.toString()
        val fechaInscripcion: String = editTextDate.text.toString()
        var aptoFisico: Number = 0
        if (chkAptoFisico.isChecked) {
            aptoFisico = 1
        }

        //verifica si el usuario ya existe
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $tablaTipoUsuario WHERE DNI=${dni}", null)
        var usuarioExiste = false
        if (cursor.moveToNext() && cursor.getInt(0) > 0) {
            usuarioExiste = true
        }
        cursor.close()

        //verifica si faltan datos
        val faltanDatos = nombre.isEmpty() || correo.isEmpty() || fechaInscripcion.isEmpty()

        if (usuarioExiste){
            mostrarAlerta(this, "Inscripción",
                "El usuario ya existe.")
                return
        }

        if (dni == 0) {
            mostrarAlerta(this, "Inscripción",
                "El DNI debe ser un número mayor a cero.")
            return
        }

        if (aptoFisico == 0) {
            mostrarAlerta(this, "Inscripción",
                "El aspirante debe tener apto físico")
            return
        }

        if (faltanDatos) {
            mostrarAlerta(
                this, "Inscripción",
                "Falta completar algún campo del formulario."
            )
            return
        }

        val values = ContentValues().apply {
            put("Nombre", nombre)
            put("DNI", dni.toInt())
            put("Correo", correo)
            put("FechaInscripcion", fechaInscripcion)
            put("AptoFisico", aptoFisico.toInt())
        }
        val newRowId = db.insert( tablaTipoUsuario, null, values)

        if (newRowId == -1L) {
            mostrarAlerta(
                this,
                "Error",
                "Hubo un error durante la inscripción."
            )
            return
        }
        mostrarAlerta(
            this,
            "Inscripción",
            "Inscripción realizada correctamente."
        )
        disableEnable(false)

        btnInscribir.isEnabled = false
        btnVerCarnet.isEnabled = true
        db.close()
    }

    private fun disableEnable(condition: Boolean) {
        disableRadioGroup(radioGroup, condition)
        btnInscribir.isEnabled = condition
        editTextNombre.isEnabled = condition
        editTextDNI.isEnabled = condition
        editTextEmail.isEnabled = condition
        editTextDate.isEnabled = condition
        chkAptoFisico.isEnabled = condition
    }

    private fun disableRadioGroup(radioGroup: RadioGroup, condition: Boolean) {
        for (i in 0 until radioGroup.childCount) {
            val view = radioGroup.getChildAt(i)
            if (view is RadioButton) {
                view.isEnabled = condition
            }
        }
    }
}