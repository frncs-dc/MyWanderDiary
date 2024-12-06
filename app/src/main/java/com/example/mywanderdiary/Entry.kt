package com.example.mywanderdiary

import android.net.Uri
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Entry(entryName: String, stringDate: String, entryContent: String, countryName: String, imageId: String, locationID: Int):
    Serializable {
    var entryName = entryName
    var countryName = countryName
    var stringDate = stringDate
    var entryContent = entryContent
    var imageId = imageId
    var locationID = locationID

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Full month name and day
        return formatter.format(date)
    }

}