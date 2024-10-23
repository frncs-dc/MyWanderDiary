package com.example.mywanderdiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.whenStarted
import com.example.mywanderdiary.databinding.ActivityViewEntryBinding

class ViewEntryActivity : AppCompatActivity(), DeleteDialogFragment.DeleteDialogListener {

    private lateinit var binding: ActivityViewEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val entry = intent.getSerializableExtra("KEY_ENTRY") as Entry

        binding.activityViewEntryCoverImage.setImageResource(entry.coverImageId)
        binding.activityEntryImage.setImageResource(entry.imageId)
        binding.activityViewEntryLocation.text = entry.locationName
        binding.activityViewEntryDateCountry.text = entry.stringDate + " - " + entry.countryName
        binding.activityViewEntryContentText.text = entry.entryContent
        binding.activityEntryMapImage.setImageResource(R.drawable.map_placeholder)

        binding.activityViewEntryBtnClose.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        binding.activityViewEntryBtnEdit.setOnClickListener{
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("KEY_ENTRY", entry)
            startActivity(intent)
        }

        binding.activityViewEntryBtnDelete.setOnClickListener{
            DeleteDialogFragment().show(supportFragmentManager, "DELETE_DIALOG")
        }
    }

    override fun onDeleteConfirmed() {
        Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

}