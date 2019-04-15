package com.example.climaaplication

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity

class Network {
    companion object {

        fun tieneRed(activity: AppCompatActivity):Boolean{
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nerworkInfo = connectivityManager.activeNetworkInfo
            return nerworkInfo != null && nerworkInfo.isConnected
        }

    }

}