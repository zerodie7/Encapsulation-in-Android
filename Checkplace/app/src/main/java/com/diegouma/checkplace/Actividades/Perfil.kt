package com.diegouma.checkplace.Actividades

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.GridView
import android.widget.TextView
import com.diegouma.checkplace.Foursquare.Foursquare
import com.diegouma.checkplace.Foursquare.User
import com.diegouma.checkplace.Foursquare.Venues
import com.diegouma.checkplace.GridViewDetalleVenue.AdaptadorGridView
import com.diegouma.checkplace.GridViewDetalleVenue.Grid
import com.diegouma.checkplace.Interfaces.UsuariosInterface
import com.diegouma.checkplace.Interfaces.VenuesLikeInterface
import com.diegouma.checkplace.R
import com.diegouma.checkplace.RecyclerViewPrincipal.AdaptadorCustom
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil.*
import java.text.NumberFormat
import java.util.*

class Perfil : AppCompatActivity() {

    /* Objeto Foursquare*/
    var fsqr: Foursquare? = null

    /*Toolbar*/
    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvFriends = findViewById<TextView>(R.id.tvFriends)
        val tvTips = findViewById<TextView>(R.id.tvTips)
        val tvPhotos = findViewById<TextView>(R.id.tvPhotos)
        val tvCheckins = findViewById<TextView>(R.id.tvCheckins)

        val grid = findViewById<GridView>(R.id.gridTabla)
        /*Iniciar arreglo GridView*/
        val listaGrid:ArrayList<Grid> = ArrayList()


        fsqr = Foursquare(this, this)

        if(fsqr?.hayToken()!!) {
            fsqr?.obtenerUsuarioActual(object : UsuariosInterface{
                override fun obtenerUsuarioActual(usuario: User) {
                    tvNombre.text = usuario.firstName
                    tvFriends.text = String.format("%d %s",usuario.friends?.count, getString(R.string.app_perfil_friends))
                    tvTips.text = String.format("%d %s", usuario.tips?.count, getString(R.string.app_perfil_tips))
                    tvPhotos.text = String.format("%d %s", usuario.photos?.count, getString(R.string.app_perfil_photos))
                    tvCheckins.text = String.format("%d %s", usuario.checkins?.count, getString(R.string.app_perfil_checkins))
                    initToolbar(usuario.firstName + " " + usuario.lastName)

                    Picasso.get().load(usuario.photo?.urlIcono).into(profile_image)

                    /*Pasar datos a la vista GridView*/
                    listaGrid.add(Grid(String.format("%s %s",NumberFormat.getNumberInstance(Locale.US).format(usuario.friends?.count), getString(R.string.app_perfil_friends)), R.drawable.ic_frieds_black_24dp, ContextCompat.getColor(applicationContext, R.color.primaryLightColor)))
                    listaGrid.add(Grid(String.format("%s %s",NumberFormat.getNumberInstance(Locale.US).format(usuario.photos?.count), getString(R.string.app_perfil_photos)), R.drawable.ic_insert_photo_black_24dp, ContextCompat.getColor(applicationContext, R.color.primaryLightColor)))
                    listaGrid.add(Grid(String.format("%s %s",NumberFormat.getNumberInstance(Locale.US).format(usuario.tips?.count), getString(R.string.app_perfil_tips)), R.drawable.ic_mode_comment_black_24dp, ContextCompat.getColor(applicationContext, R.color.primaryLightColor)))
                    listaGrid.add(Grid(String.format("%s %s",NumberFormat.getNumberInstance(Locale.US).format(usuario.checkins?.count), getString(R.string.app_perfil_checkins)), R.drawable.ic_place_black_24dp, ContextCompat.getColor(applicationContext, R.color.primaryLightColor)))

                    /*Uso de adaptador para  GridView*/
                    val adaptador = AdaptadorGridView( applicationContext, listaGrid)
                    grid.adapter = adaptador



                }
            })
        }else{
            fsqr?.mandaIniciarSession()
        }
    }

    /*Inicializacion de toolbar*/
    fun initToolbar(nombrePerfil:String){
        //Habilita la toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(nombrePerfil)
        setSupportActionBar(toolbar)

        /*Boton de retroceso y que el flujo no genere una nueva instancia*/
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }


}
