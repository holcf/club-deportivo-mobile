package com.example.clubdeportivo
import android.content.ContentValues
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


class CobroCutas : AppCompatActivity() {
    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobro_cuotas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val socios = ArrayList<Socio>()
        var selectedSocio: Socio? = null

        dbHelper = MiBaseDeDatosHelper(this)
        db = dbHelper.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM socio", null)
        while (cursor.moveToNext()) {
            val nroSocio = cursor.getInt(0)
            val nombre = cursor.getString(1)
            val dni = cursor.getInt(2)
            socios.add(Socio(nroSocio, nombre, dni))

        }
        cursor.close()
        db.close()

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        val sociosInfo = socios.map { "${it.dni} - ${it.nombre}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sociosInfo)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedInfo = parent.getItemAtPosition(position) as String
            selectedSocio = socios.find { "${it.dni} - ${it.nombre}" == selectedInfo }
            //luego para hacer el cobro usar selectedSocio.nroSocio
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
        autoCompleteTextView.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                autoCompleteTextView.performValidation()
            }
        }



        val btnCobrarCuota: Button =findViewById(R.id.btnCobrar)
        btnCobrarCuota.setOnClickListener {
            var cuota = Cuota()
            cuota.nSocio = selectedSocio?.nroSocio
            cuota.monto = findViewById<EditText>(R.id.editTextMonto).text.toString().toInt()
            cuota.fechaPago = findViewById<EditText>(R.id.editTextFechaPago).text.toString()
            cuota.fechaInicio = findViewById<EditText>(R.id.editTextInicioCuota).text.toString()
            cuota.vencimiento = findViewById<EditText>(R.id.editTextFinCuota).text.toString()

            val radioGroup: RadioGroup = findViewById(R.id.rdgMetodoPago)

            val selectedRadioButtonText: String =
                (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId))?.text.toString()
            if (selectedRadioButtonText == "Efectivo") {
                cuota.metodoPago = MetodoPago.EFECTIVO.value
            } else {
                cuota.metodoPago = MetodoPago.TARJETA.value
            }


            dbHelper = MiBaseDeDatosHelper(this)
            db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("NSocio", cuota.nSocio)
                put("Monto", cuota.monto)
                put("FechaPago", cuota.fechaPago)
                put("MetodoPago", cuota.metodoPago)
                put("FechaInicio", cuota.fechaInicio)
                put("Vencimiento", cuota.vencimiento)
            }
            val newRowId = db.insert("cuotaSocio", null, values)

            db.close()

            if (newRowId != -1L) {
                metodos.mostrarAlerta(
                    this,
                    "Inscripción",
                    "Inscripción realizada correctamente."
                )
            } else {
                metodos.mostrarAlerta(
                    this,
                    "Error",
                    "Hubo un error al insertar en la base de datos."
                )
            }

            db.close()
            //btnVerCarnet.isEnabled = true
            //btnInscribir.isEnabled = false
        }
    }
}