package com.example.clubdeportivo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Inscribir : AppCompatActivity() {
    private lateinit var editTextDate: EditText
    private lateinit var calendar: Calendar

    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase

    private fun mostrarAlerta(context: Context, titulo: String, mensaje: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Acción al hacer clic en el botón Aceptar (si es necesario)
        }
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextDate = findViewById(R.id.editTextDate)
        calendar = Calendar.getInstance()

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }



        val btnInscribir: Button =findViewById(R.id.btnInscribir)

        btnInscribir.setOnClickListener {
            dbHelper = MiBaseDeDatosHelper(this)
            db = dbHelper.writableDatabase
            // get input text values
            val nombre = findViewById<EditText>(R.id.editTextNombre).text.toString()
            val dni = findViewById<EditText>(R.id.editTextDNI).text.toString()
            val correo = findViewById<EditText>(R.id.editTextEmail).text.toString()


            //val cursor = db.rawQuery("SELECT * FROM usuario", null)
            //var loginCorrecto = false

           /* while (cursor.moveToNext()) {
                //val codUsu = cursor.getInt(0)
                val nombreUsu = cursor.getString(1)
                val passUsu = cursor.getString(2)
                //Log.d("MiBaseDeDatos", "nombre: $nombreUsu - clave: $passUsu")
                if (usuario == nombreUsu && password == passUsu){
                    loginCorrecto = true
                    break
                }
            }
            if (loginCorrecto){
                //Toast.makeText(this, "Usuario correcto", Toast.LENGTH_SHORT).show()
                DatosCompartidos.usuarioLogueado = usuario
                val intentar = Intent(this, MenuPrincipal::class.java)
                startActivity(intentar)
            }else{
                //Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
                mostrarAlerta(this, "Error de inicio de sesión", "El nombre de usuario o la contraseña son incorrectos.")

            }*/

            db.execSQL("INSERT INTO socio (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) " +
                    "VALUES (?, ?,?,?,?,?)", arrayOf(null, nombre, 12345, "f@f.com", "2021-10-10", 1))

            mostrarAlerta(this, "Inscripción", "Inscripción realizada correctamente")

            // Cerrar el cursor y la base de datos
            //cursor.close()
            db.close()

        }



    }
    private fun showDatePickerDialog() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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