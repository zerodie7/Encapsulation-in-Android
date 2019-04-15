package com.diegouma.checkplace.Interfaces

import com.google.android.gms.location.LocationResult

interface UbicacionListener {
    //Metodo para uso de localización
    fun ubicacionResponse(locationResult: LocationResult){

    }

}