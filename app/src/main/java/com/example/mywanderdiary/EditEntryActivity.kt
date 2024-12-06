package com.example.mywanderdiary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mywanderdiary.databinding.ActivityEditEntryBinding
import java.text.SimpleDateFormat

class EditEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEntryBinding
    val sdf = SimpleDateFormat("MMMM dd, yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entry = intent.getSerializableExtra("KEY_ENTRY") as Entry
        binding.activityEditEntryInputDate.setText(entry.stringDate)
        binding.activityEditEntryInputLocation.setText(entry.entryName)
        binding.activityEditEntryInputContent.setText(entry.entryContent)
        binding.imageView3.setImageResource(R.drawable.map_placeholder)

        binding.acitivityBtnEditEntry.setOnClickListener{
            entry.entryName = binding.activityEditEntryInputLocation.text.toString()
            entry.stringDate = binding.activityEditEntryInputDate.text.toString()
            entry.entryContent = binding.activityEditEntryInputContent.text.toString()

            finish()
        }

        binding.acitivityEditEntryClose.setOnClickListener{
            finish()
        }
    }
}