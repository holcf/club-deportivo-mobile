package com.example.clubdeportivo


import android.content.Context
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
//import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("SpellCheckingInspection")
class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase

    fun mostrarAlerta(context: Context, titulo: String, mensaje: String) {
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
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = MiBaseDeDatosHelper(this)
        db = dbHelper.writableDatabase

        val buttonAcceder: Button =findViewById(R.id.btnAcceder)

        // add toast to button
        buttonAcceder.setOnClickListener {

            // get input text values
            val usuario = findViewById<EditText>(R.id.editTextUsuario).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

            val cursor = db.rawQuery("SELECT * FROM usuario", null)
            var loginCorrecto = false

            while (cursor.moveToNext()) {
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
                val intentar = Intent(this, MenuPrincipal::class.java)
                startActivity(intentar)
            }else{
                //Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
                mostrarAlerta(this, "Error de inicio de sesión", "El nombre de usuario o la contraseña son incorrectos.")

            }

            // Cerrar el cursor y la base de datos
            cursor.close()
            db.close()

        }
    }



}