package com.diegouma.contactos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.ShareActionProvider
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

class Detalle : AppCompatActivity() {

    var index:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        //Habilita la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Habilita el boton back de la toolbar
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        index = intent.getStringExtra("ID").toInt()
        //Log.d("INDEX",index.toString())

        //Mapear datos a la vista
        mapeoDatos()
    }

    //Mapear datos a la vista
    fun  mapeoDatos(){
        val contacto = MainActivity.obtenerContacto(index)

        var tvNombre = findViewById<TextView>(R.id.tvNombre)
        var tvEmpresa = findViewById<TextView>(R.id.tvEmpresa)
        var tvEdad = findViewById<TextView>(R.id.tvEdad)
        var tvPeso = findViewById<TextView>(R.id.tvPeso)
        var tvTelefono = findViewById<TextView>(R.id.tvTelefono)
        var tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        var tvEmail = findViewById<TextView>(R.id.tvEmail)
        var ivFotoNueva = findViewById<ImageView>(R.id.ivFotoNuevo)

        tvNombre.text = contacto.nombre + " " + contacto.apellido
        tvEmpresa.text = contacto.empresa
        tvEdad.text = contacto.edad.toString()+ " años"
        tvPeso.text = contacto.peso.toString()+ " kg"
        tvTelefono.text = contacto.telefono
        tvDireccion.text = contacto.direccion
        tvEmail.text = contacto.email

        ivFotoNueva.setImageResource(contacto.foto)
    }

    //Asocia elementos graficos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Define accion de elemntos de la  toolbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home -> {
                finish()
                return true
            }

            R.id.itEliminar ->{
                MainActivity.eliminarContacto(index)
                finish()
                return true
            }

            R.id.itEditar->{
                val intent = Intent(this, Nuevo::class.java)
                //Envia  el id para identificar que  es  el campo de edicion y no de creacion
                intent.putExtra("ID", index.toString())

                startActivity(intent)
                return true
            }

            else ->{return super.onOptionsItemSelected(item)}
        }
    }

    //Actualiza datos cuando entra en foco  la vista aa detalle
    override fun onResume() {
        super.onResume()
        mapeoDatos()
    }

}
