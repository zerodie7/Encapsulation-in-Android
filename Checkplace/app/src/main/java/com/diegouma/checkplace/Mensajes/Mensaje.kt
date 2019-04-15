package com.diegouma.checkplace.Mensajes

import android.content.Context
import android.widget.Toast

class Mensaje {

    companion object {

        fun mensaje(context: Context, mensajes: Mensajes) {
            var str = ""
            when (mensajes) {
                Mensajes.RATIONALE -> {
                    str = "Requiero permiso para obtener ubicación"
                }
                Mensajes.CHECKIN_SUCCESS -> {
                    str = "Nuevo checkin añadido"
                }
                Mensajes.LIKE_SUCCESS -> {
                    str = "Nuevo like añadido"
                }
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }


        fun mensajeError(context: Context, error: Errores) {
            var mensaje = ""

            when (error) {
                Errores.NO_HAY_RED -> {
                    mensaje = "No se detecta ninguna conexión disponible, intenta más tarde"
                }

                Errores.HTTP_ERROR -> {
                    mensaje = "Hubo un error en la solicitud, intenta más tarde"
                }

                Errores.NO_HAY_APP_FSQR -> {
                    mensaje = "No tienes la app de Foursquare instalada, intenta más tarde"
                }
                Errores.ERROR_CONEXION_FSQR -> {
                    mensaje = "No se realizo la autenticación, inténtalo más tarde..."
                }
                Errores.INTERCAMBIO_TOKEN -> {
                    mensaje = "No se pudo obtener el Token, Inténtalo más tarde..."
                }
                Errores.ERROR_GUARDANDO_TOKEN -> {
                    mensaje = "No se pudo guardar el Token, Inténtalo más tarde..."
                }
                Errores.PERMISO_NEGADO -> {
                    mensaje = "No se dio permiso para acceder a la ubicación, Inténtalo más tarde..."
                }
                Errores.ERROR_QUERY -> {
                    mensaje = "Se genero un error en la solicitud a la API"
                }
            }
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }

        /*Se usa para mensajes propios de las APIS*/
        fun mensajeError(context: Context, error: String) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

    }
}
