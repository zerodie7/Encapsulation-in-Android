package com.diegouma.checkplace.Actividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.diegouma.checkplace.Foursquare.Category
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Foursquare.Venues
import com.diegouma.checkplace.Interfaces.ObtenerVenuesInterface
import com.diegouma.checkplace.Interfaces.UbicacionListener
import com.diegouma.checkplace.R
import com.diegouma.checkplace.RecyclerViewPrincipal.AdaptadorCustom
import com.diegouma.checkplace.RecyclerViewPrincipal.ClickListener
import com.diegouma.checkplace.RecyclerViewPrincipal.LongClickListener
import com.diegouma.checkplace.Utilidades.Ubicacion
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson

class VenuesXCategoria : AppCompatActivity() {

    var ubicacion: Ubicacion? = null

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
        setContentView(R.layout.activity_venues_xcategoria)

        fsqr = Foursquare(this, this)

        rvlista = findViewById(R.id.rvLugares)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager

        val categoriaActualString = intent.getStringExtra(Categorias.CATEGORIA_ACTUAL)
        val gson = Gson()
        val categoriaActual = gson.fromJson(categoriaActualString, Category::class.java)

        initToolbar(categoriaActual.name)

        if(fsqr?.hayToken()!!) {
            ubicacion = Ubicacion(this, object : UbicacionListener {
                override fun ubicacionResponse(locationResult: LocationResult) {

                    val lat = locationResult.lastLocation.latitude.toString()
                    val lon = locationResult.lastLocation.longitude.toString()
                    val categoryId = categoriaActual.id

                    //Toast.makeText(applicationContext, locationResult.lastLocation.latitude.toString(), Toast.LENGTH_SHORT).show()
                    fsqr?.obtenerVenues(lat, lon, categoryId ,object : ObtenerVenuesInterface {
                        override fun obtenerVenuesGenerados(venues: ArrayList<Venues>) {
                            implementacionRecylerView(venues)
                            for (venue in venues) {
                                Log.d("VENUE", venue.name)
                            }
                        }
                    })
                }
            })
        }else{
            fsqr?.mandaIniciarSession()
        }
    }

    /*Inicializacion de toolbar*/
    fun initToolbar(categoria: String){
        //Habilita la toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(categoria)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        ubicacion?.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onStart() {
        super.onStart()
        ubicacion?.inicializarUbicacion()
    }

    override fun onPause() {
        super.onPause()
        ubicacion?.detenerActualizacionUbicacion()
    }

}
