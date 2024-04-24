package com.example.clubdeportivo


 import android.content.Intent
import androidx.activity.enableEdgeToEdge
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
//import android.widget.Toast
 import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.metodos.mostrarAlerta


class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: MiBaseDeDatosHelper
    private lateinit var db: SQLiteDatabase


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
        buttonAcceder.setOnClickListener {

            val usuario = findViewById<EditText>(R.id.editTextUsuario).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

            val cursor = db.rawQuery("SELECT * FROM usuario", null)
            var loginCorrecto = false

            while (cursor.moveToNext()) {
                val nombreUsu = cursor.getString(1)
                val passUsu = cursor.getString(2)
                if (usuario == nombreUsu && password == passUsu){
                    loginCorrecto = true
                    break
                }
            }
            if (loginCorrecto){
                DatosCompartidos.usuarioLogueado = usuario
                val intentar = Intent(this, MenuPrincipal::class.java)
                startActivity(intentar)
            }else{
                //Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
                mostrarAlerta(this, "Error de inicio de sesión", "El nombre de usuario o la contraseña son incorrectos.")

            }

            cursor.close()
            db.close()

        }
    }



}