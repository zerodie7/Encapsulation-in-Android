package com.example.climaaplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var tvCiudad:TextView? = null
    var tvGrados:TextView? = null
    var tvEstatus:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicializar variables
        tvCiudad = findViewById(R.id.tvCiudad)
        tvGrados = findViewById(R.id.tvGrados)
        tvEstatus = findViewById(R.id.tvEstatus)

        val ciudad = intent.getStringExtra("com.example.climaaplication.ciudades.Ciudad")

        ///////////////////////////////////////////
        ///API: c782681dab3ae20ad1ceb86e9cdaa50d///
        ////////////Ciudad México 3530597//////////
        ////////////Ciudaad España 3118848/////////
        ////////////Ciudaad Toronto 6167865/////////
        ////////////Ciudaad Okinawa 3909360/////////
        ///////////////////////////////////////////

            if (Network.tieneRed(this)) {
                //Ejecuta solicitud HTTP
                solicitudHTTPVolley("http://api.openweathermap.org/data/2.5/weather?id="+ciudad+"&appid=c782681dab3ae20ad1ceb86e9cdaa50d&units=metric&lang=es")
            } else {
                //Muestra mensajes de error
                Toast.makeText(this, "Asegurate de estar conectado a una Red", Toast.LENGTH_SHORT).show()
            }
        //Toast.makeText(this, ciudad, Toast.LENGTH_SHORT).show()

        /*Codigo para datos duros*/
        /*
        val ciudadmx = Ciudad("Ciudad de México",15,"Soleado")
        val ciudaesp= Ciudad("Ciudad de España",2,"Nublado")

        if (ciudad=="ciudad-mexico"){
            //Muestra información de la ciudad de mexico
            tvCiudad?.text = ciudadmx.nombre
            tvGrados?.text = ciudadmx.grados.toString() + "°"
            tvEstatus?.text = ciudadmx.estatus

        } else if(ciudad=="ciudad-españa"){
            //Muestra información de la ciudad de españa
            tvCiudad?.text = ciudaesp.nombre
            tvGrados?.text = ciudaesp.grados.toString() + "°"
            tvEstatus?.text = ciudaesp.estatus

        }else{
            Toast.makeText(this, "No se encuentra información", Toast.LENGTH_SHORT).show()
        }
        */
    }

    //Metodo para volley permite manejar multiples solicitudes a la vez
    private fun solicitudHTTPVolley(url:String){

        //Cola de  espera
        val queue  = Volley.newRequestQueue(this)

        //Formulacion de la  solicitud
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String>{
                response ->
            try{
                Log.d("solicitudHTTPVolley", response)
                //Mapeo a objetos
                val gson = Gson()

                //Mapeo a clases
                val  ciudad = gson.fromJson(response, Ciudad::class.java)
                //Log.d("GSON", ciudad.name)

                //Mapeo de objetos
                tvCiudad?.text = ciudad.name
                tvGrados?.text = ciudad.main?.temp.toString() + "°"
                tvEstatus?.text =ciudad.weather?.get(0)?.description

            } catch(e: Exception){
            }
        }, Response.ErrorListener { })
        queue.add(solicitud)
    }

}
