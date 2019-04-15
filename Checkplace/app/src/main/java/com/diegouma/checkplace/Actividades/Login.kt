package com.diegouma.checkplace.Actividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.R

class Login : AppCompatActivity() {

    var fsqr: Foursquare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btLogin = findViewById<Button>(R.id.btLogin)
        fsqr = Foursquare(this, PantallaPrincipal())

        if(fsqr?.hayToken()!!) {
            fsqr?.navegarSiguienteActividad()
        }

        btLogin.setOnClickListener{
                fsqr?.iniciarSesion()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fsqr?.validarActivityResult(requestCode,resultCode,data)
    }

}
