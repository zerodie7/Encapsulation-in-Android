package com.diegouma.checkplace.Actividades

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.diegouma.checkplace.*
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Foursquare.Venues
import com.diegouma.checkplace.Interfaces.ObtenerVenuesInterface
import com.diegouma.checkplace.Interfaces.UbicacionListener
import com.diegouma.checkplace.Interfaces.VenuesLikeInterface
import com.diegouma.checkplace.RecyclerViewPrincipal.AdaptadorCustom
import com.diegouma.checkplace.RecyclerViewPrincipal.ClickListener
import com.diegouma.checkplace.RecyclerViewPrincipal.LongClickListener
import com.diegouma.checkplace.Utilidades.Ubicacion
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson

class PantallaPrincipal : AppCompatActivity() {

    var ubicacion: Ubicacion? = null

    /* Objeto Foursquare*/
    var fsqr: Foursquare? = null

    /* RecyclerViewPrincipal */
    var rvlista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    /*Toolbar*/
    var toolbar:Toolbar? = null

    /*Definir componente estatico  para la vista a detalle del intent*/
    companion object {
        val VENUE_ACTUAL = "checkplace.PantallaPrincipal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        fsqr = Foursquare(this, this)

        rvlista = findViewById(R.id.rvLugares)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        initToolbar()

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager


        if(fsqr?.hayToken()!!) {
            ubicacion = Ubicacion(this, object : UbicacionListener {
                override fun ubicacionResponse(locationResult: LocationResult) {

                    val lat = locationResult.lastLocation.latitude.toString()
                    val lon = locationResult.lastLocation.longitude.toString()

                    //Toast.makeText(applicationContext, locationResult.lastLocation.latitude.toString(), Toast.LENGTH_SHORT).show()
                    fsqr?.obtenerVenues(lat, lon, object : ObtenerVenuesInterface {
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

                intent.putExtra(VENUE_ACTUAL, venueActualString)
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

    /*Inicializacion de toolbar*/
    fun initToolbar(){
        //Habilita la toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    /*Asocia elementos graficos de la toolbar*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pantalla_principal, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /*Asocia item categorias de la toolbar*/
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.itCategorias -> {
                val intent = Intent(this,  Categorias::class.java)
                startActivity(intent)
                return true
            }

            R.id.itFavoritos -> {
                val intent = Intent(this,  Likes::class.java)
                startActivity(intent)
                return true
            }

            R.id.itPerfil -> {
                val intent = Intent(this,  Perfil::class.java)
                startActivity(intent)
                return true
            }

            R.id.itCerrarSession -> {
                fsqr?.cerrarSession()
                val intent = Intent(this,  Login::class.java)
                startActivity(intent)
                finish()//matamos ultima navegacion posibnle
                return true
            }

            else ->{return super.onOptionsItemSelected(item)}
        }
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
