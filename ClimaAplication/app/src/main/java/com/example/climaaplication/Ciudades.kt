package com.example.climaaplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class Ciudades : AppCompatActivity() {

    val TAG = "com.example.climaaplication.ciudades.Ciudad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudades)

        val btEspana = findViewById<Button>(R.id.btEspaña)
        val btMexico = findViewById<Button>(R.id.btMexico)
        val btToronto = findViewById<Button>(R.id.btToronto)
        val btOkinawa = findViewById<Button>(R.id.btOkinawa)

        btMexico.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Ciudad de México", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra(TAG,"ciudad-mexico")
            intent.putExtra(TAG,"3530597")

            startActivity(intent)
        })

        btEspana.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Ciudad de España", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra(TAG,"ciudad-españa")
            intent.putExtra(TAG,"3118848")
            startActivity(intent)
        })

        btToronto.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Ciudad de Toronto", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(TAG,"6167865")
            startActivity(intent)
        })

        btOkinawa.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Ciudad de Okinawa", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(TAG,"3909360")
            startActivity(intent)
        })

    }
}
