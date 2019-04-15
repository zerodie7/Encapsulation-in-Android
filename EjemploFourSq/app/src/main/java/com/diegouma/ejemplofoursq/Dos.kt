package com.diegouma.ejemplofoursq

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.diegouma.ejemplofoursq.RecyclerView.AdaptadorCustom
import com.diegouma.ejemplofoursq.RecyclerView.ClickListener
import com.diegouma.ejemplofoursq.RecyclerView.LongClickListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class Dos : AppCompatActivity() {
    /* Objeto Foursquare*/
    var fsq:FourSquare? = null

    /* RecyclerView */
    var rvlista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    /* Localizacion Geografica*/
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODIGO_SOLICITUD_PERMISO = 100
    private var fusedLocationClient: FusedLocationProviderClient? =  null
    private var locationRequest: LocationRequest? = null
    private var callback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dos)

        fsq = FourSquare(this)

        rvlista = findViewById(R.id.rvLista)
        rvlista?.setHasFixedSize(true) //Definimos un tamaño estandar para evitar errores de procesamiento

        layoutManager = LinearLayoutManager(this)
        rvlista?.layoutManager = layoutManager

        fusedLocationClient = FusedLocationProviderClient(this)
        inicializarLocationRequest()

        callback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                    for (ubicacion in p0?.locations!!) {
                        //Toast.makeText(applicationContext, ubicacion.latitude.toString() + " , " + ubicacion.longitude.toString(), Toast.LENGTH_SHORT).show()
                        obtenerLugares(ubicacion.latitude.toString(),ubicacion.longitude.toString())
                    }
            }
        }

    }

    fun obtenerLugares(lat:String, lon:String){

        val  queue = Volley.newRequestQueue(this)
        val  url = "https://api.foursquare.com/v2/venues/search/?ll="+lat+","+lon+"&oauth_token="+ fsq?.obtenerToken() + "&v=20190101"

        val solicitud = StringRequest(Request.Method.GET,  url, Response.Listener<String> {
            response ->
            Log.d("Response HTTP", response)

            val gson  = Gson()
            val venues = gson.fromJson(response,FoursquareRequest::class.java)
            Log.d("VENUES", venues.response?.venues?.size.toString())


            /*Mapeo de clases interfaces*/
            adaptador = AdaptadorCustom(venues.response?.venues!!, object:ClickListener{
                override fun onClick(vista: View, index: Int) {
                }
            }, object: LongClickListener {
                override fun longClick(vista: View, index: Int) {
                }
            })
            rvlista?.adapter = adaptador


        }, Response.ErrorListener {

        })
        queue.add(solicitud)
    }


    /*Inicio de funciones de ubicación*/
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
        ActivityCompat.requestPermissions(this, arrayOf(permisoFineLocation,permisoCoarseLocation), CODIGO_SOLICITUD_PERMISO)
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
    /*Fin de funciones de ubicación*/

}
