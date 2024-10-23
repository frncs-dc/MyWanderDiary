package com.example.mywanderdiary

import java.text.SimpleDateFormat

public class DataGeneration {

    companion object {
        fun createSampleEntries(): ArrayList<Entry> {
            val entries = ArrayList<Entry>()

            // Sample dates
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date1 = sdf.parse("2023-01-15")
            val date2 = sdf.parse("2023-02-20")
            val date3 = sdf.parse("2023-03-10")
            val date4 = sdf.parse("2023-12-22")

            // Adding sample entries
            entries.add(Entry(R.drawable.image1, "Tsim Sha Tsui", date1, "Visited the iconic Tsim Sha Tsui.", "Hong Kong", R.drawable.image1))
            entries.add(Entry(R.drawable.image2, "Monza Barcade", date2, "Visited the iconic Monza Barcade.", "Philippines", R.drawable.image2))
            entries.add(Entry(R.drawable.image3, "Tanghalang Haribon", date3, "Visited the iconic Tanghalang Haribon", "Philippines", R.drawable.image3))
            entries.add(Entry(R.drawable.image4, "Gundam Base Tokyo", date4, "Visited the iconic Gundam Base Tokyo", "Japan", R.drawable.image4))

            return entries
        }
    }
}