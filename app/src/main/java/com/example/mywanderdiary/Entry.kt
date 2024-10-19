package com.example.mywanderdiary

import java.io.Serializable
import java.util.Date

class Entry(locationName: String, date: Date, entryContent: String, countryName: String, imageId: Int):
    Serializable {
    var locationName = locationName
        private set
    var countryName = countryName
        private set
    var date = date
        private set
    var entryContent = entryContent
        private set
    var imageId = imageId
        private set

}