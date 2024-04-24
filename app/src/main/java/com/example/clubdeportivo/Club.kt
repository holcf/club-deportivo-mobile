package com.example.clubdeportivo

 import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
 import androidx.appcompat.app.AlertDialog

class DatosCompartidos {
    companion object {
        var usuarioLogueado: String = ""
    }
}

object BaseDatos {
      fun crear(db: SQLiteDatabase){
        // BASE DE DATOS
        // Crear tabla roles
        db.execSQL("CREATE TABLE IF NOT EXISTS roles (RolUsu INTEGER PRIMARY KEY, NomRol TEXT);")

        // Insertar datos en la tabla roles
        db.execSQL("INSERT INTO roles (RolUsu, NomRol) VALUES (120, 'Administrador'), (121, 'Empleado');")

        // Crear tabla usuario
        db.execSQL("CREATE TABLE IF NOT EXISTS usuario (CodUsu INTEGER PRIMARY KEY AUTOINCREMENT, NombreUsu TEXT, PassUsu TEXT, RolUsu INTEGER, Activo INTEGER DEFAULT 1, FOREIGN KEY (RolUsu) REFERENCES roles(RolUsu));")

        // Insertar datos en la tabla usuario
        db.execSQL("INSERT INTO usuario (NombreUsu, PassUsu, RolUsu) VALUES ('Admin', '1234', 120);")

        // Crear tabla socio
        db.execSQL("CREATE TABLE IF NOT EXISTS socio (NSocio INTEGER PRIMARY KEY, Nombre TEXT, DNI INTEGER, Correo TEXT, FechaInscripcion TEXT, AptoFisico INTEGER);")

        // Insertar datos en la tabla socio
        db.execSQL("INSERT INTO socio (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) VALUES (1, 'Socio 1', 11111111, 's1@unmail.com', '2023-10-24', 1), (2, 'Socio 2', 22222222, 's2@unmail.com', '2023-10-24', 1), (3, 'Socio 3', 33333333, 's3@unmail.com', '2023-10-24', 1);")

        // Crear tabla nosocio
        db.execSQL("CREATE TABLE IF NOT EXISTS nosocio (NSocio INTEGER PRIMARY KEY, Nombre TEXT, DNI INTEGER, Correo TEXT, FechaInscripcion TEXT, AptoFisico INTEGER);")

        // Insertar datos en la tabla nosocio
        db.execSQL("INSERT INTO nosocio (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) VALUES (1, 'No Socio 1', 11234567, 'ns1@unmail.com', '2023-10-24', 1), (2, 'No Socio 2', 22345678, 'ns2@unmail.com', '2023-10-24', 1), (3, 'No Socio 3', 33456789, 'ns3@unmail.com', '2023-10-24', 1);")

        // Crear tabla cuotaSocio
        db.execSQL("CREATE TABLE IF NOT EXISTS cuotaSocio (IdCuota INTEGER PRIMARY KEY AUTOINCREMENT, NSocio INTEGER, Monto INTEGER, FechaPago TEXT, MetodoPago INTEGER, FechaInicio TEXT, Vencimiento TEXT, FOREIGN KEY (NSocio) REFERENCES socio(NSocio));")

        // Insertar datos en la tabla cuotaSocio
        db.execSQL("INSERT INTO cuotaSocio (NSocio, Monto, FechaPago, MetodoPago, FechaInicio, Vencimiento) VALUES (1, 3000, '2023-10-13', 1, '2023-10-13', '2023-11-13'), (2, 3000, '2023-10-13', 1, '2023-10-13', '2023-11-13'), (2, 3000, '2023-11-14', 1, '2023-11-14', '2023-12-14');")

        // Crear tabla cuotaDiaria
        db.execSQL("CREATE TABLE IF NOT EXISTS cuotaDiaria (IdCuota INTEGER PRIMARY KEY AUTOINCREMENT, NSocio INTEGER, Monto INTEGER, FechaPago TEXT, MetodoPago INTEGER, FechaInicio TEXT, Vencimiento TEXT, FOREIGN KEY (NSocio) REFERENCES nosocio(NSocio));")

        // Insertar datos en la tabla cuotaDiaria
        db.execSQL("INSERT INTO cuotaDiaria (NSocio, Monto, FechaPago, MetodoPago, FechaInicio, Vencimiento) VALUES (1, 400, '2023-10-24', 1, '2023-10-24', '2023-11-24');")
    }
}
class MiBaseDeDatosHelper(context: Context) : SQLiteOpenHelper(context, NOMBRE_BD, null, VERSION_BD) {
    companion object {
        private const val NOMBRE_BD = "club.db"
        private const val VERSION_BD = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crea la tabla aquí
        //db.execSQL("CREATE TABLE libros (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT)")
        BaseDatos.crear(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Actualiza la estructura de la base de datos aquí
       // if (oldVersion == 1 && newVersion == 2) {
         //   db.execSQL("ALTER TABLE libros ADD COLUMN fecha_publicacion INTEGER")
        //}
    }
}

object metodos {
    fun mostrarAlerta(context: Context, titulo: String, mensaje: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Acción al hacer clic en el botón Aceptar (si es necesario)
        }
        builder.show()
    }
}

