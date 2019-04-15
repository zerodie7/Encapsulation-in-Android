package com.diegouma.contactos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.ShareActionProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.*

class MainActivity : AppCompatActivity() {

    var lista:ListView? = null
    var grid:GridView? = null

    //Variable para uso  del switch
    var  vsVista:ViewSwitcher? = null


    //Funciones  encapsuladas
    companion object {
        var adaptadorGrid:AdaptadorCustomGrid? = null
        var adaptador:AdaptadorCustom? = null
        var  contactos:ArrayList<Contacto>? =  null

        //Funcion para agregar  un contacto
        fun agregarContacto (contacto: Contacto){
            adaptador?.addItem(contacto)
            adaptadorGrid?.addItem(contacto)
        }

        //Funcion para obtener  contacto del layout
        fun obtenerContacto (index:Int):Contacto{
            return adaptador?.getItem(index) as Contacto
            return adaptadorGrid?.getItem(index) as Contacto

        }

        //Funcion para eliminar contactos
        fun eliminarContacto (index:Int){
            adaptador?.removeItem(index)
            adaptadorGrid?.removeItem(index)

        }

        //Funcion para actualizar contactos
        fun actualizarContacto(index:Int, nuevoContacto:Contacto){
            adaptador?.updateItem(index,nuevoContacto)
            adaptadorGrid?.updateItem(index,nuevoContacto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        contactos = ArrayList()
        contactos?.add(Contacto("Diego","Martinez","CD Project",25,60.0f,"CDMX 215","5532003352","zero@gmail.com",R.drawable.foto_01))
        contactos?.add(Contacto("jesus","Martinez","CD Project",24,60.0f,"CDMX 215","5532003352","zero@gmail.com",R.drawable.foto_02))
        contactos?.add(Contacto("Pedro","Martinez","CD Project",23,60.0f,"CDMX 215","5532003352","zero@gmail.com",R.drawable.foto_03))
        contactos?.add(Contacto("Sebastian","Martinez","CD Project",22,60.0f,"CDMX 215","5532003352","zero@gmail.com",R.drawable.foto_04))
        contactos?.add(Contacto("Irvin","Martinez","CD Project",21,60.0f,"CDMX 215","5532003352","zero@gmail.com",R.drawable.foto_05))

        lista = findViewById<ListView>(R.id.lista)
        grid = findViewById<GridView>(R.id.grid)

        /*Uso de adaptador configurado manualmente*/
        adaptador = AdaptadorCustom(this, contactos!!)

        /*Uso de adaptadorGid configurado manualmente*/
        adaptadorGrid = AdaptadorCustomGrid(this, contactos!!)

        /*Uso del switcher*/
        vsVista = findViewById(R.id.vsVista)

        //Llamada  de adaptadores
        lista?.adapter = adaptador
        grid?.adapter = adaptadorGrid

        lista?.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this,Detalle::class.java)
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }

        grid?.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this,Detalle::class.java)
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }


    }

    //Asocia elementos graficos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        //Variables para el  uso del boton de busqueda
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itBusqueda = menu?.findItem(R.id.itBusqueda)
        val searchView = itBusqueda?.actionView as SearchView

        //Variables  para uso del  boton Switch
        val itSwitch  = menu?.findItem(R.id.itSwitch)
        itSwitch?.setActionView(R.layout.switch_item)
        val switchView = itSwitch?.actionView?.findViewById<Switch>(R.id.swVista)


        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Buscar  contacto..."

        searchView.setOnQueryTextFocusChangeListener { view, b ->
            //Prepara  datos
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                //Filtra datos
                adaptador?.filtrar(p0!!)
                adaptadorGrid?.filtrar(p0!!)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                //Filtra datos
                return true
            }
        })

        switchView?.setOnCheckedChangeListener { compoundButton, b ->
            vsVista?.showNext()
        }


        return super.onCreateOptionsMenu(menu)
    }

    //Define accion de elemntos de la  toolbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.itNuevo -> {
                val intent = Intent(this,Nuevo::class.java)
                startActivity(intent)
                return true
            }
            else ->{return super.onOptionsItemSelected(item)}
        }
        return super.onOptionsItemSelected(item)
    }

    //Actualiza datos cuando entra en foco la vista main
    override fun onResume() {
        super.onResume()
        adaptador?.notifyDataSetChanged()
    }

}
