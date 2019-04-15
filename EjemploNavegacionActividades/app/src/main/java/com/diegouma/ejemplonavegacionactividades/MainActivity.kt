package com.diegouma.ejemplonavegacionactividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btB = findViewById<Button>(R.id.btB)

        btB.setOnClickListener {
            var intent = Intent(this, ActividadB::class.java)
            intent.putExtra("Mensaje",  "Hacia actividad B")
            startActivity(intent)
        }


    }
}
