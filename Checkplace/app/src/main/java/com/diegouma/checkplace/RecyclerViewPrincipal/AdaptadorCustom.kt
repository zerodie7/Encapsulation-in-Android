package com.diegouma.checkplace.RecyclerViewPrincipal

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.diegouma.checkplace.R
import com.diegouma.checkplace.Foursquare.Venues
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_venues.view.*


class AdaptadorCustom(items:ArrayList<Venues>?, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items:ArrayList<Venues>?  = null
    var multiseleccion  = false

    var itemsSeleccionados:ArrayList<Int>? = null
    var viewHolder:ViewHolder? = null

    var contexto:Context? = null

    init {
        this.items =  items
        itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val vista = LayoutInflater.from(p0.context).inflate(R.layout.template_venues, p0 , false)
        contexto  = p0.context
        viewHolder = ViewHolder(vista, listener, longClickListener)

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        //Elementos Mapeados
        val item = items?.get(p1)
        //p0.foto?.setImageResource(item?.foto!!)
        p0.nombre?.text = item?.name
        p0.state?.text = String.format("%s, %s", item?.location?.state, item?.location?.country)

        if(item?.categories?.size!! > 0 ){
            p0.category?.text = item?.categories?.get(0)?.name
        }else{
            p0.category?.setText(R.string.app_template_pantalla_principal_category)
        }

        //Cargar foto de lugares con piccaso
        Picasso.get().load(item?.imagePreview).placeholder(R.drawable.placeholder_venue).into(p0.fotoLugar)

        //Obtener icono por  libreria picasso
        //Picasso.get().load(item?.iconCategory).placeholder(R.drawable.ic_launcher_background).into(p0.foto)

        if(item?.categories?.size!! > 0){
            val urlImagen = item?.categories?.get(0)!!.icon?.prefix + "bg_64" + item?.categories?.get(0)!!.icon?.suffix
            Picasso.get().load(urlImagen).into(p0.foto)
        }

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
            var itemsEliminados =  ArrayList<Venues>()

            for(index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }
            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

    class ViewHolder(vista:View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener {

        var vista = vista
        var foto: ImageView? =  null
        var fotoLugar: ImageView? = null
        var nombre:TextView? = null
        var state:TextView? = null
        var category:TextView? = null
        var listener:ClickListener? = null
        var longClickListener:LongClickListener? = null

        init {
            this.vista = vista
            this.fotoLugar = vista.ivFotoLugar
            this.foto = vista.ivFoto //foto =  vista.findViewById(R.id.ivFoto) as ImageView si es version antigua
            this.nombre = vista.tvNombre  //nombre = vista.findViewById(R.id.tvNombre) as TextView si es version antigua
            this.state = vista.tvState
            this.category = vista.tvCategory
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