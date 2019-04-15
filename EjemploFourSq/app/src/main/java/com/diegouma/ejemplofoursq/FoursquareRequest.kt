package com.diegouma.ejemplofoursq

class FoursquareRequest {

    var response:FourSquareResponse?  = null
}

class FourSquareResponse{
    var  venues:ArrayList<Venues>? =  null
}

class Venues{
    var id:String = ""
    var name:String = ""
    var categories:ArrayList<Category>?  = null
}

class Category{
    var id:String = ""
    var name:String = ""
    var icon:Icon? = null
}

class Icon{
    var prefix:String = ""
    var suffix: String = ""
}