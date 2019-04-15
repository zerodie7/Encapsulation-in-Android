package com.diegouma.checkplace.Actividades

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.widget.*
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Interfaces.UsuariosInterface
import com.diegouma.checkplace.R
import com.diegouma.checkplace.Foursquare.User
import com.diegouma.checkplace.Foursquare.Venues
import com.diegouma.checkplace.GridViewDetalleVenue.AdaptadorGridView
import com.diegouma.checkplace.GridViewDetalleVenue.Grid
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalle_venue.*
import java.net.URLEncoder

class DetalleVenue : AppCompatActivity() {

    /*Toolbar*/
    var toolbar: Toolbar? = null
    var btCheckin: Button? = null
    var btLike: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venue)

        /*Boton Mapeo  de check-in*/
        btCheckin = findViewById(R.id.btCheckin)
        btLike = findViewById(R.id.btLike)



        val tvFoto  = findViewById<ImageView>(R.id.ivFoto)
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvState = findViewById<TextView>(R.id.tvState)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        //val tvCategory = findViewById<TextView>(R.id.tvCategory)
        //val tvcrossStreet = findViewById<TextView>(R.id.tvcrossStreet)
        //val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val grid = findViewById<GridView>(R.id.gridTabla)

        val venueActualString = intent.getStringExtra(PantallaPrincipal.VENUE_ACTUAL)

        val gson = Gson()
        val venueActual = gson.fromJson(venueActualString, Venues::class.java)

        /*Iniciar arreglo GridView*/
        val listaGrid:ArrayList<Grid> = ArrayList()


        //Log.d("VenueActual", venueActual.stats?.usersCount.toString())

        initToolbar(venueActual.name)

        Picasso.get().load(venueActual.imagePreview).placeholder(R.drawable.placeholder_venue).into(ivFoto)
        tvNombre.text = venueActual.name
        tvState.text = venueActual.location?.state
        tvCountry.text = venueActual.location?.country
        //tvAddress.text = venueActual.location?.address
        //tvcrossStreet.text = venueActual.location?.crossStreet
        //tvCategory.text = venueActual.categories?.get(0)?.name

        /*Pasar datos a la vista GridView*/
        listaGrid.add(Grid(venueActual.categories?.get(0)?.name!!, R.drawable.ic_local_offer_black_24dp, ContextCompat.getColor(this, R.color.primaryLightColor) ))
        listaGrid.add(Grid(String.format("Direccion %s",venueActual.location?.address), R.drawable.ic_place_black_24dp, ContextCompat.getColor(this, R.color.primaryLightColor)))
        listaGrid.add(Grid(String.format("Calle %s",venueActual.location?.crossStreet), R.drawable.ic_streetview_black_24dp, ContextCompat.getColor(this, R.color.primaryLightColor)))

        /*Uso de adaptador para  GridView*/
        val adaptador = AdaptadorGridView(this, listaGrid)
        grid.adapter = adaptador


        val fsqr = Foursquare(this, DetalleVenue())

        btCheckin?.setOnClickListener {
            if(fsqr.hayToken()){
                val etMensaje = EditText(this)
                etMensaje.hint = "Hola!"
                
                AlertDialog.Builder(this)
                    .setTitle("Nuevo Check-in")
                    .setMessage("Ingersa un mensaje")
                    .setView(etMensaje)
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                            dialog, which ->

                    })
                    .setPositiveButton("Check-in", DialogInterface.OnClickListener {
                            dialog, which ->

                            val mensaje =URLEncoder.encode(etMensaje.text.toString(), "UTF-8")//Se hace  un encoder a nuestro  mensaje para pasarle espacios
                            fsqr.nuevoCheckin(venueActual.id, venueActual.location!!, mensaje)
                    })
                    .show()
            }else{
                fsqr?.mandaIniciarSession()
            }
        }

        btLike?.setOnClickListener {
            if(fsqr.hayToken()){
                fsqr.nuevoLike(venueActual.id)
            }
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


}
