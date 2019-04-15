package com.diegouma.ejemplotoolbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class PantallaDos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_dos)

        //Habilita la toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        //Habilita el boton back de la toolbar
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

        //Asocia elementos graficos
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_pantalla_dos, menu)

            return super.onCreateOptionsMenu(menu)
        }
}
