package com.diegouma.databaseejemplo

import android.os.Build
import android.provider.BaseColumns

class AlumnosContrato {

    companion object {

        val VERSION = 1
        class Entrada: BaseColumns{
            companion object {
                val NOMBRE_TABLA = "alumno"

                val COLUMNA_ID = "id"
                val COLUMNA_NOMBRE = "nombre"
            }
        }
    }



}