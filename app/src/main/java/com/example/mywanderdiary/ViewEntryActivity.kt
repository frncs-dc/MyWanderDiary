package com.example.mywanderdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.databinding.ActivityViewEntryBinding

class ViewEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val entry = intent.getSerializableExtra("KEY_ENTRY") as Entry

        binding.activityEntryImage.setImageResource(entry.imageId)
        binding.activityViewEntryLocation.text = entry.locationName
        binding.activityViewEntryCountry.text = " - " + entry.countryName
        binding.activityViewEntryDate.text = entry.stringDate
        binding.activityViewEntryContent.text = entry.entryContent
        binding.activityEntryMapImage.setImageResource(R.drawable.map_placeholder)
        binding.activityViewEntryBtnClose.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }
    }

}