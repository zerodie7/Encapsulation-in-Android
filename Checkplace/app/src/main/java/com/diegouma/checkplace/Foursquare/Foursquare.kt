package com.diegouma.checkplace.Foursquare

import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.diegouma.checkplace.Actividades.Login
import com.diegouma.checkplace.Interfaces.*
import com.diegouma.checkplace.Mensajes.Errores
import com.diegouma.checkplace.Mensajes.Mensaje
import com.diegouma.checkplace.Mensajes.Mensajes
import com.diegouma.checkplace.R
import com.diegouma.checkplace.Utilidades.Network
import com.foursquare.android.nativeoauth.FoursquareOAuth
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class Foursquare(var activity: AppCompatActivity, var activityDestino:AppCompatActivity) {

        private val CODIGO_CONEXION = 200
        private val CODIGO_INTERCAMBIO_TOKEN = 201

        private val CLIENT_ID = "NXERPEHWSDPUJ4NVZNEHEBSWLQ5YRXSAOUYLA4Z2GDTLV14X"
        private val CLIENT_SECRET = "IS4GOOGVLWDLQJAJIAXNBYYFEPWX0PUX32VCGLLD5IJMBVFE"

        private val  SETTINGS = "settings"
        private val ACCESS_TOKEN = "aaccessToken"

        private val URL_BASE = "https://api.foursquare.com/v2/"
        private val VERSION = "&v=20190101"

        init {

        }

        fun iniciarSesion(){
            val intent = FoursquareOAuth.getConnectIntent(activity.applicationContext,CLIENT_ID)

            if(FoursquareOAuth.isPlayStoreIntent(intent)){
                //Muestra  mensaje  de que  no  esta instalada
                Mensaje.mensajeError(
                    activity.applicationContext,
                    Errores.NO_HAY_APP_FSQR
                )
                activity.startActivity(intent)
            }else{
                activity.startActivityForResult(intent,CODIGO_CONEXION)
            }
        }

        fun validarActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
            when(requestCode){
                CODIGO_CONEXION ->{conexionCompleta(resultCode, data)}

                CODIGO_INTERCAMBIO_TOKEN ->{intercambioTokenCompleta(resultCode,data)}
            }
        }

        private fun conexionCompleta(resultCode: Int, data: Intent?){
            val codigoRespuesta = FoursquareOAuth.getAuthCodeFromResult(resultCode, data)
            val exception = codigoRespuesta.exception

            if (exception==null){
                //Autenticacion correcta
                val codigo = codigoRespuesta.code
                realizarIntercambioToken(codigo)
            }else{
                Mensaje.mensajeError(
                    activity.applicationContext,
                    Errores.ERROR_CONEXION_FSQR
                )
            }
        }

        private fun realizarIntercambioToken(codigo:String){
            val intent = FoursquareOAuth.getTokenExchangeIntent(activity.applicationContext,CLIENT_ID,CLIENT_SECRET,codigo)
            activity.startActivityForResult(intent,CODIGO_INTERCAMBIO_TOKEN)
        }

        private fun  intercambioTokenCompleta(resultCode: Int, data: Intent?){
            val respuestaToken = FoursquareOAuth.getTokenFromResult(resultCode,data)
            val exception = respuestaToken.exception

            if (exception==null){
                val accessToken = respuestaToken.accessToken

                if(!guardarToken(accessToken)){
                    Mensaje.mensajeError(
                        activity.applicationContext,
                        Errores.ERROR_GUARDANDO_TOKEN
                    )
                    //mensaje("Token: " + accessToken)
                }else{
                    navegarSiguienteActividad()
                }
            }else{
                //Hubo problema en  obtener el token
                Mensaje.mensajeError(
                    activity.applicationContext,
                    Errores.INTERCAMBIO_TOKEN
                )
            }
        }

        fun hayToken():Boolean{
            return obtenerToken() != ""
        }

        fun guardarToken(token:String):Boolean{
            if (token.isEmpty()){
                return false
            }
            val settings = activity.getSharedPreferences(SETTINGS, 0)
            val editor = settings.edit()

            editor.putString(ACCESS_TOKEN,token)
            editor.apply()
            return true
        }

        /*Siempre eliminar todo rastro de sesion */
        fun cerrarSession(){
            val settings = activity.getSharedPreferences(SETTINGS, 0)
            val editor = settings.edit()

            editor.putString(ACCESS_TOKEN, "")
            editor.apply()
        }

        fun mandaIniciarSession(){
            activity.startActivity(Intent(this.activity, Login::class.java))
            activity.finish()
        }

        fun obtenerToken():String{
            val settings = activity.getSharedPreferences(SETTINGS, 0)
            val token = settings.getString(ACCESS_TOKEN, "")
            return token
        }

        fun navegarSiguienteActividad(){
            activity.startActivity(Intent(this.activity, activityDestino::class.java))
            activity.finish()
        }

        fun obtenerVenues(lat:String, lon:String, obtenerVenuesGenerados: ObtenerVenuesInterface){
            val network = Network(activity)

            val seccion = "venues/"
            val metodo = "search/"
            val ll = "?ll="+lat +","+ lon
            val token = "&oauth_token=" + obtenerToken()

            val url = URL_BASE +seccion +metodo +ll +token +  VERSION

            network.HttpRequest(activity.applicationContext,  url, object :
                HttpResponse {
                override fun HttpResponseSuccess(response: String) {
                    var gson  = Gson()
                    var objetoRespuesta = gson.fromJson(response, FoursquareAPIRequestVenues::class.java)

                    var meta = objetoRespuesta.meta
                    var venues = objetoRespuesta.response?.venues!!

                    if(meta?.code == 200){
                        //El Query se completo correctamente
                        for (venue in venues){
                            obtenerImagePreview(venue.id, object: ImagePreviewInterface{
                                override fun obtenerImagePreview(photos: ArrayList<Photo>) {
                                    //Operaciones cargar icono
                                    if(venue.categories?.count()!! > 0){
                                        val  urlIcono = venue.categories?.get(0)!!.icon?.construirURLImagen(obtenerToken(), VERSION, "bg_64" )
                                        venue.iconCategory = urlIcono
                                    }
                                    if (photos.count()>0){
                                        //Operaciones cargar imagenes
                                        val urlImagen = photos.get(0).construirURLImagen(obtenerToken(),VERSION, "original" )
                                        venue.imagePreview = urlImagen
                                    }
                                }
                            })
                        }
                        obtenerVenuesGenerados.obtenerVenuesGenerados(venues)
                    }else{
                        if(meta?.code  == 400){
                            Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)//Se mapea error enviado por la API de Foursquare mo uno generico
                        }else{
                            //muestra mensaje genérico
                            Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_QUERY)
                        }
                    }
                }
            })
        }

        fun obtenerVenues(lat:String, lon:String, categoryId:String, obtenerVenuesGenerados: ObtenerVenuesInterface){
        val network = Network(activity)

        val seccion = "venues/"
        val metodo = "search/"
        val ll = "?ll="+lat +","+ lon
        val categoria = "&categoryId=" + categoryId
        val token = "&oauth_token=" + obtenerToken()

        val url = URL_BASE +seccion +metodo +ll + categoria +  token +  VERSION

        network.HttpRequest(activity.applicationContext,  url, object :
            HttpResponse {
            override fun HttpResponseSuccess(response: String) {
                var gson  = Gson()
                var objetoRespuesta = gson.fromJson(response, FoursquareAPIRequestVenues::class.java)

                var meta = objetoRespuesta.meta
                var venues = objetoRespuesta.response?.venues!!

                if(meta?.code == 200){
                    //El Query se completo correctamente
                    obtenerVenuesGenerados.obtenerVenuesGenerados(venues)
                }else{
                    if(meta?.code  == 400){
                        //Existe problema de cordenadas
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            meta?.errorDetail
                        )//Se mapea error enviado por la API de Foursquare mo uno generico
                    }else{
                        //muestra mensaje genérico
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            Errores.ERROR_QUERY
                        )
                    }
                }
            }
        })
    }

        fun obtenerVenuesLike(VenuesLikeInterface: VenuesLikeInterface){
            val network = Network(activity)

            val seccion = "users/"
            val metodo = "self/"
            val token = "&oauth_token=" + obtenerToken()

            val url = URL_BASE +seccion + metodo + "venuelikes?limit=10" +  token + VERSION

            network.HttpRequest(activity.applicationContext,  url, object :
                HttpResponse {
                override fun HttpResponseSuccess(response: String) {
                    var gson  = Gson()
                    var objetoRespuesta = gson.fromJson(response, VenuesDeLikes::class.java)

                    var meta = objetoRespuesta.meta
                    var venues = objetoRespuesta.response?.venues?.items!!

                    if(meta?.code == 200){
                        //El Query se completo correctamente
                        VenuesLikeInterface.venuesGenerados(venues)
                    }else{
                        if(meta?.code  == 400){
                            //Existe problema de cordenadas
                            Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)//Se mapea error enviado por la API de Foursquare mo uno generico
                        }else{
                            //muestra mensaje genérico
                            Mensaje.mensajeError(activity.applicationContext,Errores.ERROR_QUERY)
                        }
                    }
                }
            })


        }

        fun obtenerUsuarioActual(obtenerUsuarioActual:UsuariosInterface){
            val network = Network(activity)

            val seccion = "users/"
            val metodo = "self/"
            val token = "oauth_token=" + obtenerToken()
            val query = "?" + token +  VERSION

            val url = URL_BASE +seccion + metodo + query

            network.HttpRequest(activity.applicationContext,  url, object: HttpResponse {
                override fun HttpResponseSuccess(response: String) {
                    val gson  = Gson()
                    val objetoRespuesta = gson.fromJson(response, FoursquareAPISelfUser::class.java)

                    var meta = objetoRespuesta.meta

                    if(meta?.code == 200){
                        //El Query se completo correctamente
                        val usuario = objetoRespuesta?.response?.user!!
                        usuario.photo?.construirURLImagen(obtenerToken(), VERSION,"128x128")
                        obtenerUsuarioActual.obtenerUsuarioActual(usuario)
                    }else{
                        if(meta?.code  == 400){
                            //Existe problema de cordenadas
                            Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)//Se mapea error enviado por la API de Foursquare mo uno generico
                        }else{
                            //muestra mensaje genérico
                            Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_QUERY)
                        }
                    }
                }
            })
        }

        private fun obtenerImagePreview(venueId:String, imagePreviewInterface: ImagePreviewInterface){
            val network = Network(activity)

            val seccion = "venues/"
            val metodo = "photos/"
            val token = "&oauth_token=" + obtenerToken()
            val parametros = "limit=1"

            val url = URL_BASE + seccion + venueId + "/" + metodo + "?" + parametros + token +  VERSION

            network.HttpRequest(activity.applicationContext,  url, object :
                HttpResponse {
                override fun HttpResponseSuccess(response: String) {
                    var gson  = Gson()
                    var objetoRespuesta = gson.fromJson(response, ImagePreviewVenueResponse::class.java)

                    var meta = objetoRespuesta.meta
                    var photos = objetoRespuesta.response?.photos?.items

                    if(meta?.code == 200){
                        //El Query se completo correctamente
                        imagePreviewInterface.obtenerImagePreview(photos!!)
                    }else{
                        if(meta?.code  == 400){
                            //Existe problema de cordenadas
                            Mensaje.mensajeError(
                                activity.applicationContext, meta?.errorDetail)//Se mapea error enviado por la API de Foursquare mo uno generico
                        }else{
                            //muestra mensaje genérico
                            Mensaje.mensajeError(
                                activity.applicationContext, Errores.ERROR_QUERY)
                        }
                    }
                }
            })

        }

        fun nuevoCheckin(id:String, location: Location, mensajes: String){
        val network = Network(activity)

        val seccion = "checkins/"
        val metodo = "add/"
        val token = "&oauth_token=" + obtenerToken()
        val query = "?venueId=" + id + "&shout=" + mensajes + "&ll=" + location.lat.toString() + "," +  location.lng.toString() +  token +  VERSION

        val url = URL_BASE +seccion + metodo + query

        network.HttpPOSTRequest(activity.applicationContext,  url, object : HttpResponse {
            override fun HttpResponseSuccess(response: String) {
                val gson  = Gson()
                val objetoRespuesta = gson.fromJson(response, FoursquareAPInuevoCheckin::class.java)

                var meta = objetoRespuesta.meta

                if(meta?.code == 200){
                    //El Query se completo correctamente
                    Mensaje.mensaje(activity.applicationContext, Mensajes.CHECKIN_SUCCESS)
                }else{
                    if(meta?.code  == 400){
                        //Existe problema de cordenadas
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            meta?.errorDetail
                        )//Se mapea error enviado por la API de Foursquare mo uno generico
                    }else{
                        //muestra mensaje genérico
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            Errores.ERROR_QUERY
                        )
                    }
                }
                //Log.d("NUEVO_CHECKIN", response)
            }
        })
    }

        fun nuevoLike(id:String){
        val network = Network(activity)

        val seccion = "venues/"
        val metodo = "like/"
        val token = "&oauth_token=" + obtenerToken()
        val query = "?" +  token  +  VERSION

        val url = URL_BASE + seccion + id + "/" + metodo + query

        network.HttpPOSTRequest(activity.applicationContext,  url, object : HttpResponse {
            override fun HttpResponseSuccess(response: String) {
                Log.d("NUEVO_LIKE", response)
                val gson  = Gson()
                val objetoRespuesta = gson.fromJson(response, LikeResponse::class.java)

                var meta = objetoRespuesta.meta

                if(meta?.code == 200){
                    //El Query se completo correctamente
                    Mensaje.mensaje(activity.applicationContext, Mensajes.LIKE_SUCCESS)
                }else{
                    if(meta?.code  == 400){
                        //Existe problema de cordenadas
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            meta?.errorDetail
                        )//Se mapea error enviado por la API de Foursquare mo uno generico
                    }else{
                        //muestra mensaje genérico
                        Mensaje.mensajeError(
                            activity.applicationContext,
                            Errores.ERROR_QUERY
                        )
                    }
                }
                //Log.d("NUEVO_CHECKIN", response)
            }
        })
    }

        fun cargarCategorias(obtenerCategoriasVenues:ObtenerCategoriasVenues){
            val network = Network(activity)

            val seccion = "venues/"
            val metodo = "categories/"
            val token = "&oauth_token=" + obtenerToken()
            val query = "?" + token + "&" +  VERSION

            val url = URL_BASE +seccion + metodo + query

            network.HttpRequest(activity.applicationContext,  url, object :
                HttpResponse {
                override fun HttpResponseSuccess(response: String) {
                    val gson  = Gson()
                    val objetoRespuesta = gson.fromJson(response, FoursquareAPICategorias::class.java) //Donde  se mapean  los  elementos de  la api

                    var meta = objetoRespuesta.meta

                    if(meta?.code == 200){
                        //El Query se completo correctamente
                        val categories = objetoRespuesta.response?.categories!!
                        for (categoria in categories){
                            categoria.icon?.construirURLImagen(obtenerToken(), VERSION, "bg_64")!!
                        }
                        obtenerCategoriasVenues.obtenerCategoriasVenues(objetoRespuesta.response?.categories!!)
                    }else{
                        if(meta?.code  == 400){
                            //Existe problema de cordenadas
                            Mensaje.mensajeError(
                                activity.applicationContext,
                                meta?.errorDetail
                            )//Se mapea error enviado por la API de Foursquare mo uno generico
                        }else{0
                            //muestra mensaje genérico
                            Mensaje.mensajeError(
                                activity.applicationContext,
                                Errores.ERROR_QUERY
                            )
                        }
                    }
                }
            })

    }

}