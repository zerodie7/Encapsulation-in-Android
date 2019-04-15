package com.diegouma.ejemplonavegacionactividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ActividadC : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_c)

        val  mensaje = intent.getStringExtra("Mensaje")
        val bt = findViewById<Button>(R.id.bt)



        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()

        bt.setOnClickListener {
            //Elimina actividad del  stack es  la mejor practica a  compracion de guardar mensajes en el intent
            finish()
        }


    }
}
