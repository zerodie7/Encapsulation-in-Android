package com.diegouma.ejemplotoolbar

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.support.v7.widget.SearchView
import android.support.v7.widget.ShareActionProvider
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        val btIr= findViewById<Button>(R.id.btIr)

        btIr.setOnClickListener {
            val intent = Intent(this, PantallaDos::class.java)
            startActivity(intent)
        }
    }

    //Asocia elementos graficos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        //Boton Busqueda
        val itemBusqueda = menu?.findItem(R.id.btBusqueda)
        var vistaBusqueda = itemBusqueda?.actionView as SearchView

        //Boton Compartir
        val itemCompartir = menu?.findItem(R.id.btShare)
        val shareActionProvider = MenuItemCompat.getActionProvider(itemCompartir) as ShareActionProvider
        CompartirIntent(shareActionProvider)

        vistaBusqueda.queryHint = "Escribe tu nombre..."

        vistaBusqueda.setOnQueryTextFocusChangeListener { view, b ->
            Log.d("LISTENERFOCUS",  b.toString())
        }

        //Metodos para hacer  traquin  y envio de lo que escribe el usuario
        vistaBusqueda.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("OnQueryTextChange", p0 )
                return true
            }
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d("OnQueryTextSubmit", p0)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.btFavorito -> {
                Toast.makeText(this, "Elemento añadido a favorito", Toast.LENGTH_SHORT).show()
                return true
            }
            else ->{return super.onOptionsItemSelected(item)}
        }

        return super.onOptionsItemSelected(item)
    }

    private fun CompartirIntent(shareActionProvider: ShareActionProvider){
        if(shareActionProvider!= null){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Mensaje  compartido")
            shareActionProvider.setShareIntent(intent)
        }
    }


}
