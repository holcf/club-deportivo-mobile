package com.example.clubdeportivo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SocioSpinnerAdapter(context: Context, socios: List<Socio>) :
    ArrayAdapter<Socio>(context, 0, socios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val socio = getItem(position)
        textView.text = socio?.nombre
        return view
    }
}
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
            //Toast.makeText(this@CobroCutas, "Seleccionaste: ${selectedSocio?.nombre} (ID: ${selectedSocio?.nroSocio})", Toast.LENGTH_SHORT).show()
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
        /* spinner = findViewById(R.id.spnSocio)
        val adapter = SocioSpinnerAdapter(this, socios)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedSocio = parent.getItemAtPosition(position) as Socio
                Toast.makeText(this@CobroCutas, "Seleccionaste: ${selectedSocio.nombre} (ID: ${selectedSocio.nroSocio})", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }*/

    }
}