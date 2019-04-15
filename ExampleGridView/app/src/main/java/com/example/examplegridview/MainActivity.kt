package com.example.examplegridview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var frutas:ArrayList<Fruta> = ArrayList()

        /*Datos Duros*/
        /*
        frutas.add("Manzana")
        frutas.add("Platano")
        frutas.add("Sandia")
        frutas.add("durazno")
        */

        frutas.add(Fruta("Manzana", R.drawable.manzana))
        frutas.add(Fruta("Platano", R.drawable.platano))
        frutas.add(Fruta("Sandia", R.drawable.sandia))
        frutas.add(Fruta("durazno", R.drawable.durazno))
        frutas.add(Fruta("Manzana", R.drawable.manzana))
        frutas.add(Fruta("Platano", R.drawable.platano))
        frutas.add(Fruta("Sandia", R.drawable.sandia))
        frutas.add(Fruta("durazno", R.drawable.durazno))
        frutas.add(Fruta("Manzana", R.drawable.manzana))
        frutas.add(Fruta("Platano", R.drawable.platano))
        frutas.add(Fruta("Sandia", R.drawable.sandia))
        frutas.add(Fruta("durazno", R.drawable.durazno))

        val grid = findViewById<GridView>(R.id.gridTabla)
        //val adaptador = ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, frutas)

        /*Uso de adaptador configurado manualmente*/
        val adaptador = AdaptadorCustom(this, frutas)

        grid.adapter = adaptador

        grid.onItemClickListener  = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, frutas.get(i).nombre, Toast.LENGTH_SHORT).show()
        }


    }
}
