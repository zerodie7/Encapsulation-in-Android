package com.diegouma.ejemplociclodevida

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var  nombre = "Diego"
    val  NOMBRE = "nombre"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  bt   =  findViewById<Button>(R.id.bt)
        //Toast.makeText( this, nombre,  Toast.LENGTH_SHORT).show()

        bt.setOnClickListener {
            nombre = "Ulises"
            Toast.makeText( this, nombre,  Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        nombre = savedInstanceState?.getString(NOMBRE)!!
        Toast.makeText( this, nombre,  Toast.LENGTH_SHORT).show()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(NOMBRE, nombre)
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText( this, "En transición",  Toast.LENGTH_SHORT).show()

    }

    override fun onStop() {
        super.onStop()
        Toast.makeText( this, "Aplicativo oculto",  Toast.LENGTH_SHORT).show()

    }

    //Metodo para actualizar un adaptador cuando se regresa  de una actividad
    override fun onResume() {
        super.onResume()
        Toast.makeText( this, "Aplicativo visible",  Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText( this, "App  Destruida",  Toast.LENGTH_SHORT).show()

    }

}
