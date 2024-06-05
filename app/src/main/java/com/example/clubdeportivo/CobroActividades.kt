package com.example.clubdeportivo
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CobroActividades : AppCompatActivity() {
    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var calendarPago: Calendar
    private lateinit var editTextPago: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobro_actividades)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        calendarPago = Calendar.getInstance()
        editTextPago = findViewById(R.id.editTextFechaPago)
        editTextPago.setOnClickListener {
            showDatePickerDialogPago()
        }

        //radio button rbtEfectivo por defecto
        val radioButtonToCheck: RadioButton = findViewById(R.id.rbtEfectivo)
        radioButtonToCheck.isChecked = true

        //
        // Carga datos de socios para el AutoCompleteTextView
        //
        val socios = ArrayList<Socio>()
        var selectedNoSocio: Socio? = null
        dbHelper = MiBaseDeDatosHelper(this)
        db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM nosocio", null)
        while (cursor.moveToNext()) {
            val nroSocio = cursor.getInt(0)
            val nombre = cursor.getString(1)
            val dni = cursor.getInt(2)
            socios.add(Socio(nroSocio, nombre, dni))
        }
        cursor.close()
        db.close()

        //
        // Configuración del AutoCompleteTextView
        //
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val sociosInfo = socios.map { "${it.dni} - ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sociosInfo)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedInfo = parent.getItemAtPosition(position) as String
            selectedNoSocio = socios.find { "${it.dni} - ${it.nombre}" == selectedInfo }
        }
        autoCompleteTextView.validator = object : AutoCompleteTextView.Validator {
            override fun isValid(text: CharSequence?): Boolean {
                return sociosInfo.contains(text.toString())
            }
            override fun fixText(invalidText: CharSequence?): CharSequence {
                return ""
            }
        }
        // Validar el texto cuando el campo pierda el foco
        autoCompleteTextView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                autoCompleteTextView.performValidation()
            }
        }

        //
        // Botón Cobrar Cuota
        //
        val btnCobrarActividad: Button = findViewById(R.id.btnCobrar)
        btnCobrarActividad.setOnClickListener {

            val cuota = Cuota()
            if (selectedNoSocio == null) {
                cuota.nSocio = -1
            } else {
                cuota.nSocio = selectedNoSocio?.nroSocio
            }
            if (findViewById<EditText>(R.id.editTextMonto).text.toString().isEmpty()) {
                cuota.monto = -1
            } else {
                cuota.monto = findViewById<EditText>(R.id.editTextMonto).text.toString().toInt()
            }
            cuota.fechaPago = editTextPago.text.toString()


            val radioGroup: RadioGroup = findViewById(R.id.rdgMetodoPago)
            val selectedRadioButtonText: String =
                (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()
            if (selectedRadioButtonText == "Efectivo") {
                cuota.metodoPago = MetodoPago.EFECTIVO.value
            } else {
                cuota.metodoPago = MetodoPago.TARJETA.value
            }

            if (cuota.nSocio!! <= 0) {
                metodos.mostrarAlerta(
                    this, "Cobro de actividad",
                    "El No Socio seleccionado no es válido."
                )
            } else if (cuota.monto!! <= 0) {
                metodos.mostrarAlerta(
                    this, "Cobro de actividad",
                    "El monto debe ser un número mayor a cero."
                )
            }
            else
                if (cuota.fechaPago!!.isEmpty()){
                metodos.mostrarAlerta(
                    this, "Cobro de actividad",
                    "Falta completar la fecha de pago."
                )
            }
            else {
                dbHelper = MiBaseDeDatosHelper(this)
                db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("NSocio", selectedNoSocio?.nroSocio)
                    put("Monto", cuota.monto)
                    put("FechaPago", cuota.fechaPago)
                    put("MetodoPago", cuota.metodoPago)
                }
                val newRowId = db.insert("cuotadiaria", null, values)
                db.close()

                if (newRowId != -1L) {
                    metodos.mostrarAlerta(
                        this,
                        "Cobro de actividad",
                        "Cobro de actividad realizado correctamente."
                    )
                    DatosCompartidos.cuotaPagada = cuota
                    DatosCompartidos.socioPago = selectedNoSocio
                    DatosCompartidos.tipoPago = "Actividad"
                    DatosCompartidos.tipoSocioPago = "No Socio (DNI - Nombre)"
                } else {
                    metodos.mostrarAlerta(
                        this,
                        "Error",
                        "Hubo un error al insertar en la base de datos."
                    )
                }
                val btnVerComprobante = findViewById<Button>(R.id.btnVerComprobante)
                btnVerComprobante.isEnabled = true
                btnCobrarActividad.isEnabled = false
            }
        }

        //
        // Botón nuevo cobro
        //
        val btnNuevoCobro = findViewById<Button>(R.id.btnNuevoCobro)
        val btnVerComprobante = findViewById<Button>(R.id.btnVerComprobante)
        btnNuevoCobro.setOnClickListener {
            // Limpia los campos del formulario
            findViewById<EditText>(R.id.editTextMonto).setText("")
            editTextPago.setText("")
            autoCompleteTextView.setText("")
            btnVerComprobante.isEnabled = false
            btnCobrarActividad.isEnabled = true
            radioButtonToCheck.isChecked = true
        }

        //
        // Botón Ver Comprobante
        //
        btnVerComprobante.setOnClickListener {
            val intent = Intent(this, ComprobantePago::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialogPago() {
        val currentYear = calendarPago.get(Calendar.YEAR)
        val currentMonth = calendarPago.get(Calendar.MONTH)
        val currentDay = calendarPago.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendarPago.set(Calendar.YEAR, year)
                calendarPago.set(Calendar.MONTH, month)
                calendarPago.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate = dateFormat.format(calendarPago.time)
                editTextPago.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

}