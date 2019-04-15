package com.diegouma.ejemploubicacion

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener


class MainActivity : AppCompatActivity() {

    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val CODIGO_SOLICITUD_PERMISO = 100

    var fusedLocationClient: FusedLocationProviderClient? =  null

    var locationRequest:LocationRequest? = null

    var callback:LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)
        inicializarLocationRequest()

        callback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                for(ubicacion in p0?.locations!!){
                    Toast.makeText(applicationContext, ubicacion.latitude.toString()+ " , " + ubicacion.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun inicializarLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 5000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this, permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinaria = ActivityCompat.checkSelfPermission(this,  permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return   hayUbicacionOrdinaria && hayUbicacionPrecisa
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){

        /*
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this,object : OnSuccessListener<Location>{
            override fun onSuccess(p0: Location?) {
                if(p0 != null){
                    Toast.makeText(applicationContext, p0?.latitude.toString()+ " - " + p0?.latitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        */
        fusedLocationClient?.requestLocationUpdates(locationRequest,callback, null)
    }

    private fun pedirPermisos(){
        val proveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(this, permisoFineLocation)

        if(proveerContexto){
            //Manda mensaje  con explicacion  adicional
            solicitudPermiso()
        }else{
            solicitudPermiso()
        }
    }

    private fun solicitudPermiso(){
        //Aqui puden ir más permisos si son necesarios
        requestPermissions(arrayOf(permisoFineLocation,permisoCoarseLocation), CODIGO_SOLICITUD_PERMISO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CODIGO_SOLICITUD_PERMISO ->{
                if(grantResults.size > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Obtener ubicación
                    obtenerUbicacion()
                }else{
                    Toast.makeText(this, "No se dio permiso para acceder a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun detenerActualizacionUbicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }

    override fun onStart() {
        super.onStart()

        if(validarPermisosUbicacion()){
            obtenerUbicacion()
        }else{
            pedirPermisos()
        }
    }

    override fun onPause() {
        super.onPause()

        detenerActualizacionUbicacion()
    }

}
