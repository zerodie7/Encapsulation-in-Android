package com.example.soilicitudhttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.security.Policy

class MainActivity : AppCompatActivity(), CompletadoListener{

    override fun descargaCompleta(resultado: String) {
        Log.d("descargaCompleta", resultado)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btValidarRed = findViewById<Button>(R.id.btValidarRed)
        val btSolicitudHTTP = findViewById<Button>(R.id.btSolicitudHTTP)
        val btVolley = findViewById<Button>(R.id.btVolley)
        val btOk = findViewById<Button>(R.id.btOk)

        btValidarRed.setOnClickListener {
            //Codigo de validación de red
            if (Network.tieneRed(this)) {
                Toast.makeText(this, "Hay red!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Asegurate de estar conectado a una Red", Toast.LENGTH_SHORT).show()
            }
        }

        btSolicitudHTTP.setOnClickListener {
            if (Network.tieneRed(this)) {
                //Log.d("btSolicitudOnClick",descargarDatos("http://www.google.com"))
                DescargaURL(this).execute("http://www.google.com")
            } else {
                Toast.makeText(this, "Asegurate de estar conectado a una Red", Toast.LENGTH_SHORT).show()
            }
        }

        btVolley.setOnClickListener {
            if (Network.tieneRed(this)) {
                    solicitudHTTPVolley("http://www.google.com")
            } else {
                Toast.makeText(this, "Asegurate de estar conectado a una Red", Toast.LENGTH_SHORT).show()
            }
        }

        btOk.setOnClickListener {
            if (Network.tieneRed(this)) {
                    solicitudOkHTTP("http://www.google.com")
            } else {
                Toast.makeText(this, "Asegurate de estar conectado a una Red", Toast.LENGTH_SHORT).show()
            }
        }
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
                } catch(e:Exception){

                }
            }, Response.ErrorListener { })

            queue.add(solicitud)
        }
    
        //Metodo para OkHttp
        private fun solicitudOkHTTP(url:String) {

            //Creacion de cliente
            val cliente = OkHttpClient()
            val solicitud = okhttp3.Request.Builder().url(url).build()

            cliente.newCall(solicitud).enqueue(object :okhttp3.Callback{

                override fun onFailure(call: Call, e: IOException) {
                    //implementacion de  error
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val resultado = response.body()?.string()

                    this@MainActivity.runOnUiThread {
                        try{
                            Log.d("solicitudOkHTTP", resultado)
                        } catch(e:Exception){

                        }
                    }
                }
            })
        }

}
