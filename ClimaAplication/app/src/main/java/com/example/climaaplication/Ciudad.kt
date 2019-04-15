package com.example.climaaplication

class Ciudad(name:String, weather:ArrayList<Weather>, main:Main){

/*Datos  duros*/
/*
    var nombre:String = ""
    var grados:Int = 0
    var estatus:String = ""

    init {
        this.nombre = nombre
        this.grados = grados
        this.estatus = estatus
    }
*/

    var name:String = ""
    var weather:ArrayList<Weather>? = null
    var main:Main? = null

    init {
        this.name = name
        this.weather = weather
        this.main = main
    }

}