package com.diegouma.checkplace.Actividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.diegouma.checkplace.Foursquare.Category
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Interfaces.ObtenerCategoriasVenues
import com.diegouma.checkplace.R
import com.diegouma.checkplace.RecyclerViewCategorias.AdaptadorCustom
import com.diegouma.checkplace.RecyclerViewCategorias.ClickListener
import com.diegouma.checkplace.RecyclerViewCategorias.LongClickListener
import com.google.gson.Gson

class Categorias : AppCompatActivity() {

    /* RecyclerViewCategories */
    var rvlista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    /*Toolbar*/
    var toolbar: Toolbar? = null

    /*Definir componente estatico  para la vista a detalle del intent*/
    companion object {
        val CATEGORIA_ACTUAL = "checkplace.Categorias"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        initToolbar()
        initRecyclerView()

        val frsq = Foursquare(this, Categorias())

        if(frsq.hayToken()){
            frsq.cargarCategorias(object: ObtenerCategoriasVenues{
                override fun obtenerCategoriasVenues(categorias: ArrayList<Category>) {
                    Log.d("CATEGORIAS", categorias.count().toString())

                    implementacionRecylerView(categorias)
                }
            })
        }else{
            frsq.mandaIniciarSession()
        }

    }


    private fun initRecyclerView(){
        rvlista = findViewById(R.id.rvCategorias)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager
    }


    /* RecyclerView */
    private fun implementacionRecylerView(categorias: ArrayList<Category>){
        /*Mapeo de clases categorias*/
        adaptador = AdaptadorCustom( categorias, object: ClickListener {
            override fun onClick(vista: View, index: Int) {
                val categoriaToJson = Gson()
                val categoriaActualString = categoriaToJson.toJson(categorias.get(index))
                /*Definicion de intent vista a detalle*/
                val  intent = Intent(applicationContext, VenuesXCategoria::class.java)
                Log.d("CATEGORIA_ACTUAL", categoriaActualString)

                intent.putExtra(Categorias.CATEGORIA_ACTUAL, categoriaActualString)
                startActivity(intent)

            }
        }, object: LongClickListener {
            override fun longClick(vista: View, index: Int) {
            }
        })
        rvlista?.adapter = adaptador
    }

    /*Inicializacion de toolbar*/
    fun initToolbar(){
        //Habilita la toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_categorias)
        setSupportActionBar(toolbar)

        /*Boton de retroceso y que el flujo no genere una nueva instancia*/
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }

    }


}
