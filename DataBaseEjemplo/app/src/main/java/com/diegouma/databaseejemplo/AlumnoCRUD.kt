package com.diegouma.databaseejemplo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class AlumnoCRUD (context: Context) {

    private var helper:DBHelper? = null


    init {
        helper = DBHelper(context)
    }

    //Insert
    fun newAlumno(item:Alumno){
        //Abrir DB en modo escritura
        val db = helper?.writableDatabase!!

        //Mapeo de columnas con valores a insertar
        val values = ContentValues()
        values.put(AlumnosContrato.Companion.Entrada.COLUMNA_ID, item.id)
        values.put(AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE, item.nombre)

        //Insertar nueva fila en la tabla
        val newRowId = db.insert(AlumnosContrato.Companion.Entrada.NOMBRE_TABLA, null, values)

        //Cerrar DB
        db.close()
    }

    //Select
    fun getAlumnos():ArrayList<Alumno> {

        val items:ArrayList<Alumno> = ArrayList()

        //Abrir DB en modo lectura
        val db = helper?.readableDatabase!!

        //Especificar columnas a consultar
        val columnas = arrayOf(AlumnosContrato.Companion.Entrada.COLUMNA_ID,
                                            AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE)

        //Crear cursor para recorrer tabla
        val c:Cursor = db.query(AlumnosContrato.Companion.Entrada.NOMBRE_TABLA,
                                columnas,
                                null,
                                null,
                                null,
                                null,
                                null
                                )

        //Hacer recorrido del cursor en la tabla
        while (c.moveToNext()){
            items.add(Alumno(
                c.getString(c.getColumnIndexOrThrow(AlumnosContrato.Companion.Entrada.COLUMNA_ID)),
                c.getString(c.getColumnIndexOrThrow(AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE))
                ))
        }

        //Cerrar DB
        db.close()

        return items
    }

    //Select Where
    fun getAlumno(id:String): Alumno {

        var item:Alumno? = null

        //Abrir DB en modo lectura
        val db = helper?.readableDatabase!!

        //Especificar columnas a consultar
        val columnas = arrayOf(AlumnosContrato.Companion.Entrada.COLUMNA_ID,
                                            AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE)

        //Crear cursor para recorrer tabla
        val c:Cursor = db.query(AlumnosContrato.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            "id = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )

        //Hacer recorrido del cursor en la tabla
        while (c.moveToNext()){
            item = Alumno(c.getString(c.getColumnIndexOrThrow(AlumnosContrato.Companion.Entrada.COLUMNA_ID)),
                          c.getString(c.getColumnIndexOrThrow(AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE)))
        }

        //Cerrar DB
        db.close()

        return item!!
    }

    //Update
    fun updateAlumno(item: Alumno) {

        //Abrir DB en modo lectura
        val db = helper?.writableDatabase!!

        val values = ContentValues()
        values.put(AlumnosContrato.Companion.Entrada.COLUMNA_ID, item.id)
        values.put(AlumnosContrato.Companion.Entrada.COLUMNA_NOMBRE, item.id)

        db.update(
            AlumnosContrato.Companion.Entrada.NOMBRE_TABLA,
            values,
            "id = ?",
            arrayOf(item.id)
        )

        //Cerrar DB
        db.close()
    }

    //Delete
    fun deleteAlumno(item: Alumno) {

        //Abrir DB en modo lectura
        val db = helper?.writableDatabase!!

        db.delete(AlumnosContrato.Companion.Entrada.NOMBRE_TABLA,
            "id = ?",
            arrayOf(item.id)
            )

        //Cerrar DB
        db.close()
    }
}