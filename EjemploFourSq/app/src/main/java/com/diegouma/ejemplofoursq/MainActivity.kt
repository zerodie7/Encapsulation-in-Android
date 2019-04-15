package com.diegouma.ejemplofoursq

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    var fsq:FourSquare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fsq = FourSquare(this)

        val   btLoguear = findViewById<Button>(R.id.btLoguear)

        if(fsq?.hayToken()!!) {
            startActivity(Intent(this, Dos::class.java))
            finish()
        }

        btLoguear.setOnClickListener{
                fsq?.iniciarSesion()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fsq?.validarActivityResult(requestCode,resultCode,data)
    }



}
