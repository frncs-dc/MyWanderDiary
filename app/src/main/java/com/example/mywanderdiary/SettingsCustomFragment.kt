package com.example.mywanderdiary

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywanderdiary.database.LocationDatabase
import com.example.mywanderdiary.databinding.FragmentSettingsCustomBinding

class SettingsCustomFragment : Fragment(R.layout.fragment_settings_custom) {
    private lateinit var homeBinding: FragmentSettingsCustomBinding;
    private lateinit var locationDatabase: LocationDatabase
    // Initialize locationDatabase in onAttach instead of onViewCreated
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to initialize now that the fragment is attached to the context
        locationDatabase = LocationDatabase(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentSettingsCustomBinding.inflate(inflater, container, false)

        homeBinding.rvIgnored.adapter = LocationAdapter(LocationDatabase.cachedLocations)
        homeBinding.rvIgnored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return homeBinding.root
    }
}