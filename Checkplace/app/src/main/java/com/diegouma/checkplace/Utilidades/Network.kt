package com.diegouma.checkplace.Utilidades

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.diegouma.checkplace.Interfaces.HttpResponse
import com.diegouma.checkplace.Mensajes.Errores
import com.diegouma.checkplace.Mensajes.Mensaje

class Network(var activity:AppCompatActivity ) {

    fun  hayRed():Boolean{
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo !=  null && networkInfo.isConnected
    }

    fun HttpRequest(context: Context, url: String, httpResponse: HttpResponse){

        if(hayRed()){
            val queue = Volley.newRequestQueue(context)

            val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> {

                    response ->

                httpResponse.HttpResponseSuccess(response)

            }, Response.ErrorListener {
                    error ->

                Log.d("HTTP_REQUEST", error.message.toString())

                Mensaje.mensajeError(context, Errores.HTTP_ERROR)
            })
            queue.add(solicitud)
        }else{
            Mensaje.mensajeError(context, Errores.NO_HAY_RED)
        }
    }

    fun HttpPOSTRequest(context: Context, url: String, httpResponse: HttpResponse){

        if(hayRed()){
            val queue = Volley.newRequestQueue(context)

            val solicitud = StringRequest(Request.Method.POST, url, Response.Listener<String> {

                    response ->

                httpResponse.HttpResponseSuccess(response)

            }, Response.ErrorListener {
                    error ->

                Log.d("HTTP_REQUEST", error.message.toString())

                Mensaje.mensajeError(context, Errores.HTTP_ERROR)
            })
            queue.add(solicitud)
        }else{
            Mensaje.mensajeError(context, Errores.NO_HAY_RED)
        }
    }

}