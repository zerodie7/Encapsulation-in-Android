package com.example.examplelistview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var frutas:ArrayList<Fruta> = ArrayList()
        frutas.add(Fruta("Manzana", R.drawable.manzana))
        frutas.add(Fruta("Platano", R.drawable.platano))
        frutas.add(Fruta("Sandia", R.drawable.sandia))
        frutas.add(Fruta("durazno", R.drawable.durazno))

        val lista = findViewById<ListView>(R.id.lista)
        //val adaptador = ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, frutas)
        /*Uso de adaptador configurado manualmente*/
        val adaptador = AdaptadorCustom(this, frutas)


        lista.adapter  = adaptador

        lista.onItemClickListener  = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, frutas.get(i).nombre, Toast.LENGTH_SHORT).show()
        }

    }
}
