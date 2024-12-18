package com.example.mywanderdiary

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class Entry(coverImageId: Int, locationName: String, date: Date, entryContent: String, countryName: String, imageId: Int):
    Serializable {
    var coverImageId = coverImageId
    var locationName = locationName
    var countryName = countryName
    var date = date
    var stringDate: String = formatDate(date)
    var entryContent = entryContent
    var imageId = imageId

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMMM dd, yyyy") // Full month name and day
        return formatter.format(date)
    }

}