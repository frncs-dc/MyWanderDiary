package com.example.mywanderdiary

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywanderdiary.database.Database
import com.example.mywanderdiary.databinding.ActivityMainBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding

class HomeFragment:Fragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding;
    private lateinit var locationDatabase: Database
    private lateinit var entryAdapter: EntryAdapter

    private val addItemResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Refresh the RecyclerView data when an item is added
                refreshRecyclerViewData()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        locationDatabase = Database(requireContext())
        locationDatabase.initializeCachedLocations()
        entryAdapter = EntryAdapter(locationDatabase.getEntries())
        homeBinding.rvEntries.adapter = entryAdapter
        homeBinding.rvEntries.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return homeBinding.root
    }

    // Method to refresh the RecyclerView data after adding an item
    private fun refreshRecyclerViewData() {
        // Fetch the updated data from the database
        val updatedEntries = locationDatabase.getEntries()
        // Update the adapter with the new data
        entryAdapter.updateEntries(updatedEntries)
    }

}
