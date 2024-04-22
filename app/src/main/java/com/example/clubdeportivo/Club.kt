package com.example.clubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatosCompartidos {
    companion object {
        var usuarioLogueado: String = ""
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Actualiza la estructura de la base de datos aquí
       // if (oldVersion == 1 && newVersion == 2) {
         //   db.execSQL("ALTER TABLE libros ADD COLUMN fecha_publicacion INTEGER")
        //}
    }
}

