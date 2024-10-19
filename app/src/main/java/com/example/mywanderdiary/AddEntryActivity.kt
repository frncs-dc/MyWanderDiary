package com.example.mywanderdiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mywanderdiary.databinding.ActivityAddEntryBinding

class AddEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.acitivityBtnAddEntry.setOnClickListener {
            finish()
        }

        binding.acitivityAddEntryClose.setOnClickListener {
            finish()
        }

    }
}