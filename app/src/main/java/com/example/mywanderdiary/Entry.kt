package com.example.mywanderdiary

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class Entry(locationName: String, date: Date, entryContent: String, countryName: String, imageId: Int):
    Serializable {
    var locationName = locationName
        private set
    var countryName = countryName
        private set
    var date = date
        private set
    var stringDate: String = formatDate(date)
        private set
    var entryContent = entryContent
        private set
    var imageId = imageId
        private set

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMMM dd, yyyy") // Full month name and day
        return formatter.format(date)
    }

}