package com.example.mywanderdiary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mywanderdiary.databinding.ActivityAddEntryBinding

class AddEntry : AppCompatActivity() {

    private lateinit var binding: ActivityAddEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.acitivityBtnAddEntry.setOnClickListener {
            finish()
        }

        binding.acitivityBtnAddEntryClose.setOnClickListener {
            finish()
        }

    }
}