package com.example.first_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btsaludar = findViewById<Button>(R.id.btSaludar)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val cbDev = findViewById<CheckBox>(R.id.cbDeveloper)

        btsaludar.setOnClickListener(View.OnClickListener {
            if(validarDato()){
                if(cbDev.isChecked)
                    {
                        Toast.makeText( this, "Bienvenido, " + etNombre.text + " ,eres un desarrollador", Toast.LENGTH_LONG).show()
                    } else{
                    Toast.makeText( this, "Bienvenido, " + etNombre.text, Toast.LENGTH_LONG).show()
                }
                } else{
                Toast.makeText(this, "Escribe tu nombre para saludarta", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun validarDato(): Boolean{

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val nombreUsuario = etNombre.text

        if(nombreUsuario.isNullOrEmpty()){
            return false
        }

        return true
    }
}
