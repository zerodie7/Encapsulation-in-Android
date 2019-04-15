package com.diegouma.checkplace.Utilidades

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.diegouma.checkplace.Interfaces.UbicacionListener
import com.diegouma.checkplace.Mensajes.Errores
import com.diegouma.checkplace.Mensajes.Mensaje
import com.diegouma.checkplace.Mensajes.Mensajes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class Ubicacion (var activity: AppCompatActivity, ubicacionListener: UbicacionListener) {

    /* Localizacion Geografica*/
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODIGO_SOLICITUD_PERMISO = 100

    private var fusedLocationClient: FusedLocationProviderClient? =  null
    private var locationRequest: LocationRequest? = null
    private var callback: LocationCallback? = null

    init {
        fusedLocationClient =  FusedLocationProviderClient(activity.applicationContext)
        inicializarLocationRequest()

        callback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                ubicacionListener.ubicacionResponse(p0!!)
            }
        }
    }

    /*Inicio de funciones de ubicación*/
    private fun inicializarLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 5000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(activity.applicationContext, permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinaria = ActivityCompat.checkSelfPermission(activity.applicationContext,  permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return   hayUbicacionOrdinaria && hayUbicacionPrecisa
    }

    private fun pedirPermisos(){
        val proveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(activity, permisoFineLocation)

        if(proveerContexto){
            //Manda mensaje  con explicacion  adicional
            Mensaje.mensaje(activity.applicationContext, Mensajes.RATIONALE)
        }
        solicitudPermiso()
    }

    private fun solicitudPermiso(){
        //Aqui puden ir más permisos si son necesarios
        ActivityCompat.requestPermissions(activity, arrayOf(permisoFineLocation,permisoCoarseLocation), CODIGO_SOLICITUD_PERMISO)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            CODIGO_SOLICITUD_PERMISO ->{
                if(grantResults.size > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Obtener ubicación
                    obtenerUbicacion()
                }else{
                    Mensaje.mensajeError(activity.applicationContext, Errores.PERMISO_NEGADO)
                }
            }
        }
    }

     fun detenerActualizacionUbicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }

     fun inicializarUbicacion(){
        if (validarPermisosUbicacion()){
            obtenerUbicacion()
        }else{
            pedirPermisos()
        }
    }

    @SuppressLint("MissingPermission") //Directiva para ignorar el error
    private fun obtenerUbicacion(){
        validarPermisosUbicacion()
        fusedLocationClient?.requestLocationUpdates(locationRequest,callback, null)
    }

}