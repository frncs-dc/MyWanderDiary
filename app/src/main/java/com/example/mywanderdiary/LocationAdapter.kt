package com.example.mywanderdiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.SettingsIgnoredLocBinding

class LocationAdapter(private val locations: ArrayList<Location>): RecyclerView.Adapter<LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemViewBinding: SettingsIgnoredLocBinding = SettingsIgnoredLocBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return LocationViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        // this returns the size of ALL locations
        // this adapter should have only been for LocationType.IGNORED
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        if (locations.get(position).LOCATION_TYPE == LocationType.IGNORED){
            holder.bindData(locations.get(position))
        }
    }
}