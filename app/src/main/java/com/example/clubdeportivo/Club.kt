package com.example.clubdeportivo

 import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
 import androidx.appcompat.app.AlertDialog

class DatosCompartidos {
    companion object {
        var usuarioLogueado: String = ""
        var cuotaPagada: Cuota? = null
        var socioPago: Socio? = null
        var tipoPago: String = ""
        var tipoSocioPago: String = ""

    }


}

data class Cuota(
    var nSocio: Int?=null,
    var monto: Int?=null,
    var fechaPago: String?=null,
    var metodoPago: Int?=null,
    var fechaInicio: String?=null,
    var vencimiento: String?=null
)

enum class MetodoPago(val value: Int) {
    EFECTIVO(0),
    TARJETA(1)
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
          db.execSQL("INSERT INTO socio (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) VALUES " +
                  "(4, 'Carlos Pérez', 44444444, 'carlos.perez@unmail.com', '2023-10-24', 1), " +
                  "(5, 'María López', 55555555, 'maria.lopez@unmail.com', '2023-10-24', 1), " +
                  "(6, 'Juan García', 66666666, 'juan.garcia@unmail.com', '2023-10-24', 1), " +
                  "(7, 'Ana Martínez', 77777777, 'ana.martinez@unmail.com', '2023-10-24', 1), " +
                  "(8, 'Luis Rodríguez', 88888888, 'luis.rodriguez@unmail.com', '2023-10-24', 1), " +
                  "(9, 'Elena Fernández', 99999999, 'elena.fernandez@unmail.com', '2023-10-24', 1), " +
                  "(10, 'Pedro Gómez', 10101010, 'pedro.gomez@unmail.com', '2023-10-24', 1), " +
                  "(11, 'Laura Sánchez', 20202020, 'laura.sanchez@unmail.com', '2023-10-24', 1), " +
                  "(12, 'José Díaz', 30303030, 'jose.diaz@unmail.com', '2023-10-24', 1), " +
                  "(13, 'Carmen Ramírez', 40404040, 'carmen.ramirez@unmail.com', '2023-10-24', 1), " +
                  "(14, 'Miguel Torres', 50505050, 'miguel.torres@unmail.com', '2023-10-24', 1), " +
                  "(15, 'Isabel Ruiz', 60606060, 'isabel.ruiz@unmail.com', '2023-10-24', 1), " +
                  "(16, 'Antonio Hernández', 70707070, 'antonio.hernandez@unmail.com', '2023-10-24', 1), " +
                  "(17, 'Patricia Jiménez', 80808080, 'patricia.jimenez@unmail.com', '2023-10-24', 1), " +
                  "(18, 'Javier Morales', 90909090, 'javier.morales@unmail.com', '2023-10-24', 1), " +
                  "(19, 'Marta Ortiz', 11121314, 'marta.ortiz@unmail.com', '2023-10-24', 1), " +
                  "(20, 'Francisco Castro', 21222324, 'francisco.castro@unmail.com', '2023-10-24', 1), " +
                  "(21, 'Lucía Gutiérrez', 31323334, 'lucia.gutierrez@unmail.com', '2023-10-24', 1), " +
                  "(22, 'Hugo Romero', 41424344, 'hugo.romero@unmail.com', '2023-10-24', 1), " +
                  "(23, 'Sara Molina', 51525354, 'sara.molina@unmail.com', '2023-10-24', 1);")

        // Crear tabla nosocio
        db.execSQL("CREATE TABLE IF NOT EXISTS nosocio (NSocio INTEGER PRIMARY KEY, Nombre TEXT, DNI INTEGER, Correo TEXT, FechaInscripcion TEXT, AptoFisico INTEGER);")

        // Insertar datos en la tabla nosocio
        db.execSQL("INSERT INTO nosocio (NSocio, Nombre, DNI, Correo, FechaInscripcion, AptoFisico) VALUES (1, 'No Socio 1', 11234567, 'ns1@unmail.com', '2023-10-24', 1), (2, 'No Socio 2', 22345678, 'ns2@unmail.com', '2023-10-24', 1), (3, 'No Socio 3', 33456789, 'ns3@unmail.com', '2023-10-24', 1);")

        // Crear tabla cuotaSocio
        db.execSQL("CREATE TABLE IF NOT EXISTS cuotaSocio (IdCuota INTEGER PRIMARY KEY AUTOINCREMENT, NSocio INTEGER, Monto INTEGER, FechaPago TEXT, MetodoPago INTEGER, FechaInicio TEXT, Vencimiento TEXT, FOREIGN KEY (NSocio) REFERENCES socio(NSocio));")

        // Insertar datos en la tabla cuotaSocio
        db.execSQL("INSERT INTO cuotaSocio (NSocio, Monto, FechaPago, MetodoPago, FechaInicio, Vencimiento) VALUES (1, 3000, '2023-10-13', 1, '2023-10-13', '2023-11-13'), (2, 3000, '2023-10-13', 1, '2023-10-13', '2023-11-13'), (2, 3000, '2023-11-14', 1, '2023-11-14', '2023-12-14');")
          db.execSQL("INSERT INTO cuotaSocio (NSocio, Monto, FechaPago, MetodoPago, FechaInicio, Vencimiento) VALUES " +
                  "(4, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(5, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(6, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(7, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(8, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(9, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(10, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(11, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(12, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(13, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05'), " +
                  "(14, 3000, '2023-11-15', 1, '2023-11-15', '2024-05-05');")
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

