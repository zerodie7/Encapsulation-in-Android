package com.diegouma.checkplace.Foursquare

import android.media.Image

class FoursquareAPIRequestVenues {
    var meta: Meta? = null
    var response: FourSquareResponseVenue?  = null
}

class FoursquareAPInuevoCheckin{
    var meta: Meta? = null
}

class Meta{
    var code:Int = 0
    var errorDetail:String = ""
}

class FourSquareResponseVenue{
    var  venues:ArrayList<Venues>? =  null
}

class Venues{
    var id:String = ""
    var name:String = ""
    var location: Location? = null
    var categories:ArrayList<Category>?  = null

    var imagePreview: String? = null
    var iconCategory:String?  = null
}

class Location{
    var lat:Double = 0.0
    var lng:Double = 0.0
    var address:String = ""
    var crossStreet:String = ""
    var state:String = ""
    var country:String = ""
}

class Category{
    var id:String = ""
    var name:String = ""
    var icon: Icon? = null

    var pluralName:String = ""
    var shortName:String = ""
}

open class Icon{
    var prefix:String = ""
    var suffix: String = ""
    var urlIcono:String = ""

    fun construirURLImagen(tokenAccess:String, version:String, size:String):String{
        val prefix = prefix
        val suffix = suffix
        val token = "oauth_token=" + tokenAccess
        val url = prefix + size + suffix +  "?" + token + version
        urlIcono = url
        return url
    }
}

class FoursquareAPISelfUser{
    var meta: Meta? = null
    var response: FourSquareResponseSelfUser?  = null
}

class FourSquareResponseSelfUser {
    var user: User? = null
}

class User {
    var  id  = ""
    var firstName = ""
    var lastName = ""
    var photo: Photo? = null
    var friends: Friends? = null
    var tips: Tips? = null
    var photos: Photos? = null
    var checkins: Checkins? = null
}

class Photo: Icon() { // ya  se eredaron las propiedades de  icon siufix
    var id = ""
    var width = 0
    var height = 0
}

class Friends {
    var count = 0
}

class Tips {
    var count = 0
}

class Photos {
    var count  = 0
    var items: ArrayList<Photo>? = null
}


class Checkins {
    var count  = 0
    var items:ArrayList<Checkin>? = null
}

class Checkin {
    var shout = ""
    var venue: Venues? = null
}

class FoursquareAPICategorias{
    var meta:Meta? =  null
    var response: CategoriasResponse?  = null
}

class CategoriasResponse {
    var categories:ArrayList<Category>? = null
}

class LikeResponse{
    var meta:Meta? = null
}

class VenuesDeLikes{
    var meta: Meta? = null
    var response: VenuesDeLikeResponse?  = null
}

class VenuesDeLikeResponse {
 var venues:VenuesDeLikesObject? = null
}

class VenuesDeLikesObject {
    var items:ArrayList<Venues>? = null
}

class ImagePreviewVenueResponse{
    var meta:Meta? = null
    var response: PhotosResponse? = null
}

class PhotosResponse {
    var photos: Photos? = null
}