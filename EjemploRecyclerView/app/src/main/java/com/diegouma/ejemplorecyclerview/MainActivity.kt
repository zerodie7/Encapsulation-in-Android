package com.diegouma.ejemplorecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var rvlista:RecyclerView? = null
    var adaptador:AdaptadorCustom? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    //Definicion de ActionMode
    var isActionMode =  false
    var actionMode:ActionMode? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val platillos = ArrayList<Platillo>()

        platillos.add(Platillo("Comida1", 250.0,3.5F, R.drawable.comida1))
        platillos.add(Platillo("Comida2", 150.0,1.5F, R.drawable.comida2))
        platillos.add(Platillo("Comida3", 350.0,4.5F, R.drawable.comida3))
        platillos.add(Platillo("Comida4", 450.0,2.5F, R.drawable.comida4))
        platillos.add(Platillo("Comida5", 500.0,4.5F, R.drawable.comida5))
        platillos.add(Platillo("Comida6", 200.0,5.0F, R.drawable.comida6))



        rvlista = findViewById(R.id.rvLista)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager

        //////////////////////Definicion de ActionMode///////////////
        val callback = object: ActionMode.Callback{

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {

                when(p1?.itemId){
                    R.id.itEliminar ->{
                        adaptador?.eliminarSeleccionados()
                    }
                    else->{
                        return true
                    }
                }

                adaptador?.terminarActionMode()
                p0?.finish()
                isActionMode=false

                return true
            }

            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                //Inicializa el Action Mode
                adaptador?.iniciarActionMode()
                actionMode = p0
                //Inflar  menú, pinta el logo  de eliminar en  la barra ya estable
                menuInflater.inflate(R.menu.menu_contextual, p1!!)
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p0?.title = "0 Seleccionados"
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                //Destruye el Action Mode
                adaptador?.destruirActionMode()
                isActionMode = false
            }
        }
        //////////////////////////////////////////////////////////////////////

        /*Mapeo de clases interfaces*/
        adaptador = AdaptadorCustom(platillos, object:ClickListener{
            override fun onClick(vista: View, index: Int) {
                Toast.makeText(applicationContext, platillos.get(index).nombre, Toast.LENGTH_SHORT).show()
            }
        }, object: LongClickListener{
            override fun longClick(vista: View, index: Int) {
                //Log.d("LONG","Prueba.....")
                if(!isActionMode){
                    startSupportActionMode(callback)
                    isActionMode  =  true
                    adaptador?.seleccionarItem(index)
                }else{
                    //Hacer seleccion o deseleccion
                    adaptador?.seleccionarItem(index)
                }

                actionMode?.title = adaptador?.NumeroElementosSeleccionados().toString() +  "  seleccionados"
            }

        })

        rvlista?.adapter = adaptador



        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.srActualiza)
        swipeToRefresh.setOnRefreshListener{

            for(i  in  1..1000000000){

            }
            swipeToRefresh.isRefreshing = false
            platillos.add(Platillo("Comida4", 450.0,2.5F, R.drawable.comida4))
            adaptador?.notifyDataSetChanged()
            //Log.d("REFRESH","La informacion se esta actualizando.....")
        }



    }
}
