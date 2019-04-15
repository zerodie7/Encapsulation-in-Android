package com.diegouma.ejemplorecyclerview

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class AdaptadorCustom(items:ArrayList<Platillo>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items:ArrayList<Platillo>?  = null
    var multiseleccion  = false

    var itemsSeleccionados:ArrayList<Int>? = null
    var viewHolder:ViewHolder? = null

    init {
        this.items =  items
        itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdaptadorCustom.ViewHolder {
        val vista = LayoutInflater.from(p0.context).inflate(R.layout.template_comida, p0 , false)
        viewHolder = ViewHolder(vista, listener, longClickListener)

        return viewHolder!!
    }

    override fun getItemCount(): Int {

        return items?.count()!!

    }

    override fun onBindViewHolder(p0: AdaptadorCustom.ViewHolder, p1: Int) {

        //Elementos Mapeados
        val item = items?.get(p1)
        p0.foto?.setImageResource(item?.foto!!)
        p0.nombre?.text = item?.nombre
        p0.precio?.text = "$ " + item?.precio.toString()
        p0.rating?.rating = item?.rating!!

        if (itemsSeleccionados?.contains(p1)!!){
            p0.vista.setBackgroundColor(Color.LTGRAY)
        }else{
            p0.vista.setBackgroundColor(Color.WHITE)
        }
    }

    /*Funciones de implementacion de barra de acción*/

    fun iniciarActionMode(){
        multiseleccion = true
    }

    fun destruirActionMode(){
        multiseleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        //Eliminar elementos seleccionados
        for (item  in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiseleccion = false
        notifyDataSetChanged()
    }

    fun seleccionarItem(index:Int){
        if(multiseleccion){
            if(itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }
            notifyDataSetChanged()
        }
    }

    fun NumeroElementosSeleccionados():Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if(itemsSeleccionados?.count()!! > 0){
            //Eliminar conbase en contenido
            var itemsEliminados =  ArrayList<Platillo>()

            for(index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }
            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

    class ViewHolder(vista:View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener {

        var vista = vista
        var foto:ImageView? =  null
        var nombre:TextView? = null
        var precio:TextView? = null
        var rating:RatingBar? = null

        var  listener:ClickListener? = null

        var longClickListener:LongClickListener? = null

        init {
            foto =  vista.findViewById(R.id.ivPlatillo)
            nombre = vista.findViewById(R.id.tvPlatillo)
            precio = vista.findViewById(R.id.tvPrecio)
            rating  = vista.findViewById(R.id.rbCalificacion)

            this.listener =listener
            vista.setOnClickListener(this)

            this.longClickListener = longClickListener
            vista.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener?.onClick(p0!!, adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            this.longClickListener?.longClick(p0!!, adapterPosition)
            return true
        }
    }

}