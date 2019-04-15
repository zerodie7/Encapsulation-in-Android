package com.diegouma.contactos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*

class Nuevo : AppCompatActivity() {

    //Variables globales para la seleccion de foto
    var fotoIndex:Int = 0

    val fotos = arrayOf(R.drawable.foto_01,R.drawable.foto_02,R.drawable.foto_03,R.drawable.foto_04,R.drawable.foto_05,R.drawable.foto_06)

    var foto:ImageView? = null

    //Index para la edicion de datos
    var index:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo)

        //Habilita la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Habilita el boton back de la toolbar
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Habilitar el la seleccion de fotos
        foto = findViewById<ImageView>(R.id.ivFotoNuevo)
        foto?.setOnClickListener {
            seleccionarFoto()
        }

        //Reconocer  acción  de editar vs nuevo ("hasExtra" permite saber si hay  un  elemento en un inten)
         if(intent.hasExtra("ID")){
            index = intent.getStringExtra("ID").toInt()
             rellenarDatos(index)
         }

    }

    //Asocia elementos graficos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Define accion de elemntos de la  toolbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){

            android.R.id.home -> {
                finish()
                return true
            }

            R.id.itCrearNuevo -> {
                //Crea nuevo elemento del tipo  contacto
                val etNombre =  findViewById<EditText>(R.id.etNombre)
                val etApellido =  findViewById<EditText>(R.id.etApellido)
                val etEmpresa =  findViewById<EditText>(R.id.etEmpresa)
                val etEdad =  findViewById<EditText>(R.id.etEdad)
                val etPeso =  findViewById<EditText>(R.id.etPeso)
                val etTelefono =  findViewById<EditText>(R.id.etTelefono)
                val etDireccion =  findViewById<EditText>(R.id.etDireccion)
                val etEmail =  findViewById<EditText>(R.id.etEmail)

                //Validacion de campos vacios
                var  campos  = ArrayList<String>()
                campos.add(etNombre.text.toString())
                campos.add(etApellido.text.toString())
                campos.add(etEmpresa.text.toString())
                campos.add(etEdad.text.toString())
                campos.add(etPeso.text.toString())
                campos.add(etTelefono.text.toString())
                campos.add(etDireccion.text.toString())
                campos.add(etEmail.text.toString())

                var i = 0
                for(campo in campos) {
                    if (campo.isNullOrEmpty())
                        i++
                }
                    if(i > 0 ){
                        Toast.makeText(this,"Rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }else  {
                        if(index > -1){
                            MainActivity.actualizarContacto(index,Contacto(campos.get(0),campos.get(1),campos.get(2),campos.get(3).toInt(),campos.get(4).toFloat(),campos.get(5),campos.get(6),campos.get(7),obtenerFotos(fotoIndex)))
                        }else{
                            //Agrega los elementos mapeados
                            MainActivity.agregarContacto(Contacto(campos.get(0),campos.get(1),campos.get(2),campos.get(3).toInt(),campos.get(4).toFloat(),campos.get(5),campos.get(6),campos.get(7),obtenerFotos(fotoIndex)))
                        }
                        finish()
                        Log.d("No Elementos", MainActivity.contactos?.count().toString())
                    }
                return true
            }
            else ->{return super.onOptionsItemSelected(item)}
        }
    }

    //Funcion para seleccionar fotos
    fun seleccionarFoto(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona imagen de perfil")

        val adaptadorDialogo =  ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Foto 01")
        adaptadorDialogo.add("Foto 02")
        adaptadorDialogo.add("Foto 03")
        adaptadorDialogo.add("Foto 04")
        adaptadorDialogo.add("Foto 05")
        adaptadorDialogo.add("Foto 06")

        builder.setAdapter(adaptadorDialogo){
                dialogInterface, i ->
                fotoIndex = i
            foto?.setImageResource(obtenerFotos(fotoIndex))
        }

        builder.setNegativeButton("Cancelar"){
                dialogInterface, i ->
                dialogInterface.dismiss()
        }
        builder.show()
    }

    //Funcion para seleccionar fotos
    fun obtenerFotos(index:Int):Int{
        return fotos.get(index)
    }

    //Funcion para rellenar datos en el campo editable
    fun  rellenarDatos(index: Int){
        val  contacto = MainActivity.obtenerContacto(index)

        val etNombre =  findViewById<EditText>(R.id.etNombre)
        val etApellido =  findViewById<EditText>(R.id.etApellido)
        val etEmpresa =  findViewById<EditText>(R.id.etEmpresa)
        val etEdad =  findViewById<EditText>(R.id.etEdad)
        val etPeso =  findViewById<EditText>(R.id.etPeso)
        val etTelefono =  findViewById<EditText>(R.id.etTelefono)
        val etDireccion =  findViewById<EditText>(R.id.etDireccion)
        val etEmail =  findViewById<EditText>(R.id.etEmail)
        var ivFotoNueva = findViewById<ImageView>(R.id.ivFotoNuevo)

        etNombre.setText(contacto.nombre, TextView.BufferType.EDITABLE)
        etApellido.setText(contacto.apellido, TextView.BufferType.EDITABLE)
        etEmpresa.setText(contacto.empresa, TextView.BufferType.EDITABLE)
        etEdad.setText(contacto.edad.toString(), TextView.BufferType.EDITABLE)
        etPeso.setText(contacto.peso.toString(), TextView.BufferType.EDITABLE)
        etTelefono.setText(contacto.telefono, TextView.BufferType.EDITABLE)
        etDireccion.setText(contacto.direccion, TextView.BufferType.EDITABLE)
        etEmail.setText(contacto.email, TextView.BufferType.EDITABLE)

        ivFotoNueva.setImageResource(contacto.foto)

        var posicion = 0
        for(foto in fotos){
            if(contacto.foto == foto) {
                fotoIndex = posicion
            }
                posicion++
            }
        }

}
