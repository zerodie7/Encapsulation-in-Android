package com.diegouma.databaseejemplo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):SQLiteOpenHelper (context, AlumnosContrato.Companion.Entrada.NOMBRE_TABLA,null,AlumnosContrato.Companion.VERSION) {

    companion object {
        val CREATE_ALUMNOS_TABLA = "CREATE TABLE" + AlumnosContrato.Companion.Entrada.NOMBRE_TABLA +
                " (" + AlumnosContrato.Companion.Entrada.COLUMNA_ID + "TEXT PRIMARY KEY, " +
                   AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE + "TEXT)"

        val REMOVE_ALUMNOS_TABLA = "DROP TABLE IF EXISTS" + AlumnosContrato.Companion.Entrada.NOMBRE_TABLA
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(CREATE_ALUMNOS_TABLA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(REMOVE_ALUMNOS_TABLA)
        onCreate(db)
    }
}