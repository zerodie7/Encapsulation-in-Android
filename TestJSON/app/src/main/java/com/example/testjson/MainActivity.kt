package com.example.testjson

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    /*
    var listaPersonas:ArrayList<Persona>? = null
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var respuesta = "{ \"personas\" : [ " +
                                    "{" +
                                    " \"nombre\" : \"Marcos\" ," +
                                    " \"pais\" : \"México\" ," +
                                    " \"estado\" : \"soltero\" ," +
                                    " \"experiencia\" : 5}," +

                                    "{" +
                                    " \"nombre\" : \"Agustín\" ," +
                                    " \"pais\" : \"España\" ," +
                                    " \"estado\" : \"casado\" ," +
                                    " \"experiencia\" : 16}" +
                                    " ]" +
                                    " }"
        //Mapeo a objetos
        val gson = Gson()
        //Mapeo a clases
        val  res = gson.fromJson(respuesta, Personas::class.java)
        Log.d("GSON", res.personas?.count().toString())

        ////////* Codigo para parseo de json de forma nativa*///////
        /*
        val json = JSONObject(respuesta)
        val personas =  json.getJSONArray("personas")
        //Inicializar el array
        listaPersonas = ArrayList()

        for(i in 0..personas.length()-1){
            val nombre = personas.getJSONObject(i).getString("nombre")
            val pais = personas.getJSONObject(i).getString("pais")
            val estado = personas.getJSONObject(i).getString("estado")
            val experiencia = personas.getJSONObject(i).getInt("experiencia")

            //Asociar los datos a un objeto (mapear respuesta de un json a objetos)
            //val persona = Persona(nombre,pais,estado,experiencia)
            listaPersonas?.add(Persona(nombre,pais,estado,experiencia))
            //Log.d("Persona", persona.nombre)

        }

        Log.d("onCreate", listaPersonas?.count().toString())
          */
    }



}
