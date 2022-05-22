package com.example.getmymaps.models

import java.io.Serializable

//place is a marker with a particular locaiton on map with title and description of whats there
data class Place (

    val title:String, val description: String, val latitude: Double, val longitude: Double):Serializable{

}