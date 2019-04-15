package com.diegouma.ejemplonavegacionactividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ActividadB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_b)

        val btC = findViewById<Button>(R.id.btC)
        val btD = findViewById<Button>(R.id.btD)

        val  mensaje = intent.getStringExtra("Mensaje")

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()

        btC.setOnClickListener {
            var intent = Intent(this, ActividadC::class.java)
            intent.putExtra("Mensaje",  "Hacia actividad C")
            startActivity(intent)

        }

        btD.setOnClickListener {
            var intent = Intent(this, ActividadD::class.java)
            intent.putExtra("Mensaje",  "Hacia actividad D")
            startActivity(intent)

        }

    }
}
