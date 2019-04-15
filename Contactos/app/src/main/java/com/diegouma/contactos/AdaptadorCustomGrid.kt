package com.diegouma.contactos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class AdaptadorCustomGrid(var context: Context, items:ArrayList<Contacto>): BaseAdapter() {


    //Almacenar  elementos a mostrar en el ListView
    var items: ArrayList<Contacto>? = null

    //Copia de todos los elementos de items para no modificar los elemntos originales
    var copiaItems: ArrayList<Contacto>? = null

    init {
        //Se realiza un deepCopy para que estos tengan el mismo contenido
        this.items = ArrayList(items)
        this.copiaItems = items
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var holder: ViewHolder? = null
        var vista: View? = p1

        if (vista == null) {
            vista = LayoutInflater.from(context).inflate(R.layout.template_contacto_grid, null)
            holder = ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag  as? ViewHolder
        }

        val item = getItem(p0) as Contacto

        //Asignacion de valores a elementos graficos
        holder?.nombre?.text = item.nombre + " " + item.apellido
        holder?.foto?.setImageResource(item.foto)
        return vista!!
    }

    override fun getItem(p0: Int): Any {
        return items?.get(p0)!!
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return items?.count()!! //Doble  signo  de exclamacion para obtener el  valor
    }

    /*Funciones encapsuladas en el adaptador para usar la funcion de busqueda*/
    fun addItem(item:Contacto){
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

    fun removeItem(index:Int){
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

    fun updateItem(index:Int, newItem:Contacto){
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

    fun filtrar(str:String){
        items?.clear()
        if(str.isEmpty()){
            items = ArrayList(copiaItems)
            notifyDataSetChanged()
            return
        }
        var busqueda = str
        busqueda = busqueda.toLowerCase()

        //usa !! para  obtener el contenido
        for(item in copiaItems!!){
            val nombre = item.nombre.toLowerCase()

            if(nombre.contains(busqueda)){
                items?.add(item)
            }
        }
        notifyDataSetChanged()
    }
    ////////////////////////////////////////////////////////////////////////////

    private class ViewHolder(vista: View) {
        var nombre: TextView? = null
        var foto: ImageView? = null

        init {
            nombre = vista.findViewById(R.id.tvNombre)
            foto = vista.findViewById(R.id.ivFoto)
        }
    }
}