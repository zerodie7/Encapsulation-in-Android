package com.diegouma.ejemplomapas

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private lateinit var mMap: GoogleMap

    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODIGO_SOLICITUD_PERMISO = 100
    private var fusedLocationClient: FusedLocationProviderClient? =  null
    private var locationRequest: LocationRequest? = null
    private var callback: LocationCallback? = null

    private var listaMarcadores: ArrayList<Marker>? = null

    private var miPosicion:LatLng? = null

    //Marcadores  de  mapa
    private var marcadorGolden:Marker? = null
    private var marcadorPiramide:Marker? = null
    private var marcadorTorre:Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = FusedLocationProviderClient(this)
        inicializarLocationRequest()

        callback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                if(mMap != null) {
                    //Requiere  una previa implementacion de servicios
                    mMap.isMyLocationEnabled  = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true

                    for (ubicacion in p0?.locations!!) {
                        Toast.makeText(applicationContext, ubicacion.latitude.toString() + " , " + ubicacion.longitude.toString(), Toast.LENGTH_SHORT).show()

                        miPosicion = LatLng(ubicacion.latitude, ubicacion.longitude)
                        mMap.addMarker(MarkerOptions().position(miPosicion!!).title("Aquí estoy"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miPosicion))
                    }
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






    private fun cargarURL(url:String){
        val  queue = Volley.newRequestQueue(this)

        val solicitud  = StringRequest(Request.Method.GET, url, Response.Listener<String>{

                response ->
            Log.d("HTTP", response)

        }, Response.ErrorListener {  })

        queue.add(solicitud)
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Tipos de  mapa
        //mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        cambiarEstiloMapa()

        marcadoresEstaticos()

        crearListeners()

        prepararMarcadores ()

        dibujarLinea()

    }


    private  fun cambiarEstiloMapa(){
        val cambioMapa = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.estilo_mapas))

        if(!cambioMapa){
            //Menciona si tiene problema para el cambio  del mapa
        }
    }

    private fun crearListeners(){
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMarkerDragListener(this)
    }

    private fun marcadoresEstaticos(){
        val  GOLDEN_GATE = LatLng(37.8199286,-122.4782551)
        val  PIRAMIDE = LatLng(29.9772962,31.1324955)
        val  TORRE = LatLng(43.722952, 10.396597)

        marcadorGolden = mMap.addMarker(MarkerOptions()
            .position(GOLDEN_GATE)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.train_transportation))
            .snippet("Metro de San Francisco")
            .alpha(0.6f)
            .title("Golden Gate"))
        marcadorGolden?.tag = 0

        marcadorPiramide = mMap.addMarker(MarkerOptions()
            .position(PIRAMIDE)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            .snippet("Metro de Giza")
            .alpha(0.6f)
            .title("Piramides"))
        marcadorPiramide?.tag = 0

        marcadorTorre = mMap.addMarker(MarkerOptions()
            .position(TORRE)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            .snippet("Metro de pisa")
            .alpha(0.6f)
            .title("Torre de Pisa"))
        marcadorTorre?.tag = 0

    }

    private fun prepararMarcadores (){

        listaMarcadores = ArrayList()

        mMap.setOnMapLongClickListener {
            location: LatLng? ->

            listaMarcadores?.add(mMap.addMarker(MarkerOptions()
                    .position(location!!)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .snippet("Metro de pisa")
                    .alpha(0.6f)
                    .title("Torre de Pisa"))
            )
            listaMarcadores?.last()!!.isDraggable = true

            val coordenadas = LatLng(listaMarcadores?.last()!!.position.latitude, listaMarcadores?.last()!!.position.longitude)

            val origen = "origin=" + miPosicion?.latitude + "," + miPosicion?.longitude +  "&"

            val destino = "destination=" + coordenadas.latitude + "," + coordenadas.longitude + "&"

            val key = "key=AIzaSyABKvT1RE6GTo0yGgs1Y7kFgHIrV-68qvI"

            val parametros = origen + destino  + "sensor=false&mode=driving&" + key

            Log.d("URL","https://maps.googleapis.com/maps/api/directions/json?" + parametros )

            cargarURL("https://maps.googleapis.com/maps/api/directions/json?" + parametros)


        }
    }

    private fun  dibujarLinea(){

        val coordenadasLineas = PolylineOptions()
            .add(LatLng(19.434200011141158,-99.1477056965232))
            .add(LatLng(19.44104913340259,-99.13651446044444))
            .add(LatLng(19.44404092953131,-99.13057102054359))
            .add(LatLng(19.437794547975827,-99.13751095533371))
            .pattern(arrayListOf<PatternItem>(Dot(),Gap(15f)))
            .color(Color.GREEN)
            .width(10f)

       val coordenadasPoligono = PolygonOptions()
            .add(LatLng(19.434200011141158,-99.1477056965232))
            .add(LatLng(19.44104913340259,-99.14651446044444))
            .add(LatLng(19.44404092953131,-99.14057102054359))
            .add(LatLng(19.437794547975827,-99.13751095533371))
            .strokePattern(arrayListOf<PatternItem>(Dash(10f),Gap(15f)))
            .strokeColor(Color.BLUE)
            .fillColor(Color.TRANSPARENT)
            .strokeWidth(10f)

        val coordenadasCirculo = CircleOptions()
            .center(LatLng(19.434200011141158,-99.1477056965232))
            .radius(420.0)
            .strokePattern(arrayListOf<PatternItem>(Dash(10f),Gap(10f)))
            .strokeWidth(10f)
            .strokeColor(Color.RED)
            .fillColor(Color.TRANSPARENT)


        mMap.addPolyline(coordenadasLineas)
        mMap.addPolygon(coordenadasPoligono)
        mMap.addCircle(coordenadasCirculo)


    }





    override fun onMarkerDragEnd(marcador: Marker?) {
        Toast.makeText(this,"Terminaste de mover el marcador", Toast.LENGTH_SHORT).show()
        val index = listaMarcadores?.indexOf(marcador!!)

    }

    override fun onMarkerDragStart(marcador: Marker?) {
        Toast.makeText(this,"Estas moviendo tu marcador", Toast.LENGTH_SHORT).show()
        val index = listaMarcadores?.indexOf(marcador!!)
    }

    override fun onMarkerDrag(marcador: Marker?) {
        title = marcador?.position?.latitude.toString() + " - " + marcador?.position?.latitude.toString()
    }

    override fun onMarkerClick(marcador: Marker?): Boolean {
        var numeroClicks = marcador?.tag as Int

        if(numeroClicks != null){
            numeroClicks++
            marcador.tag =  numeroClicks

            Toast.makeText(this, "Se   han dado "+ numeroClicks.toString()  + " clicks", Toast.LENGTH_SHORT).show()
        }

        return false
    }

}
