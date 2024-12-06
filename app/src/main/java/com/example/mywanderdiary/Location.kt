package com.example.mywanderdiary

class Location(
    var LOCATION_ID: Int,
    var LOCATION_NAME: String,
    var LOCATION_TYPE: LocationType,
    var LOCATION_LAT: Double,
    var LOCATION_LON: Double
) {

    fun setLocationName(name: String) {
        LOCATION_NAME = name
    }
}