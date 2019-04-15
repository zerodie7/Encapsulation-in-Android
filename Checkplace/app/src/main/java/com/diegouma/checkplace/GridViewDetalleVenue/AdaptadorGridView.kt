package com.diegouma.checkplace.GridViewDetalleVenue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.diegouma.checkplace.R
import kotlinx.android.synthetic.main.template_gridview_detalle_venues.view.*

class AdaptadorGridView(var  context: Context, items:ArrayList<Grid>): BaseAdapter() {


        var items:ArrayList<Grid>? = null

        init {
            this.items = items
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var holder:ViewHolder? = null
            var vista: View?  = p1

            if (vista == null){
                vista = LayoutInflater.from(context).inflate(R.layout.template_gridview_detalle_venues,null)
                holder = ViewHolder(vista)
                vista.tag = holder
            }else{
                holder = vista.tag as? ViewHolder
            }

            val item = getItem(p0) as Grid
            holder?.nombre?.text = item.nombre
            holder?.imagen?.setImageResource(item.icon)
            holder?.containerColor?.setBackgroundColor(item.color)

            return vista!!
        }

        override fun getItem(p0: Int): Any {
            return items?.get(p0)!!
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return items?.count()!! //Doble  signo  de exclamacion para obtener el valor
        }

        private class ViewHolder(vista: View){
            var nombre: TextView? = null
            var imagen: ImageView? = null
            var containerColor:LinearLayout? = null

            init {
                nombre = vista.nombre
                imagen = vista.imagen
                containerColor = vista.conteinerColor
            }
        }
    }