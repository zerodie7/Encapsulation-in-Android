package com.diegouma.checkplace.Actividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Foursquare.Venues
import com.diegouma.checkplace.Interfaces.ObtenerVenuesInterface
import com.diegouma.checkplace.Interfaces.UbicacionListener
import com.diegouma.checkplace.Interfaces.VenuesLikeInterface
import com.diegouma.checkplace.R
import com.diegouma.checkplace.RecyclerViewPrincipal.AdaptadorCustom
import com.diegouma.checkplace.RecyclerViewPrincipal.ClickListener
import com.diegouma.checkplace.RecyclerViewPrincipal.LongClickListener
import com.diegouma.checkplace.Utilidades.Ubicacion
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson

class Likes : AppCompatActivity() {

    /* Objeto Foursquare*/
    var fsqr: Foursquare? = null

    /* RecyclerViewPrincipal */
    var rvlista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    /*Toolbar*/
    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)

        fsqr = Foursquare(this, this)

        rvlista = findViewById(R.id.rvLugares)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager

        initToolbar()


        if(fsqr?.hayToken()!!) {
                    fsqr?.obtenerVenuesLike(object : VenuesLikeInterface{
                        override fun venuesGenerados(venues: ArrayList<Venues>) {
                            implementacionRecylerView(venues)
                        }

                    })
        }else{
            fsqr?.mandaIniciarSession()
        }

    }

    /*Inicializacion de toolbar*/
    fun initToolbar(){
        //Habilita la toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_favoritos)
        setSupportActionBar(toolbar)

        /*Boton de retroceso y que el flujo no genere una nueva instancia*/
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }

    /* RecyclerView */
    private fun implementacionRecylerView(lugares: ArrayList<Venues>){
        /*Mapeo de clases interfaces*/
        adaptador = AdaptadorCustom(lugares, object: ClickListener {
            override fun onClick(vista: View, index: Int) {
                //Toast.makeText(applicationContext, lugares.get(index).name,  Toast.LENGTH_SHORT).show()
                val venueToJson = Gson()
                val venueActualString = venueToJson.toJson(lugares.get(index))
                //Log.d("VENUE_ACTUAL_STRING", venueActualString)
                /*Definicion de intent vista a detalle*/
                val  intent = Intent(applicationContext, DetalleVenue::class.java)
                Log.d("VenueActual", venueActualString)

                intent.putExtra(PantallaPrincipal.VENUE_ACTUAL, venueActualString)
                startActivity(intent)
            }
        }, object: LongClickListener {
            override fun longClick(vista: View, index: Int) {
            }
        })
        rvlista?.adapter = adaptador
    }

}
