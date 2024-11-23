package com.example.mywanderdiary

import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.SettingsIgnoredLocBinding

class LocationViewHolder (private val viewBinding: SettingsIgnoredLocBinding): RecyclerView.ViewHolder(viewBinding.root){
    fun bindData(location: Location){
        this.viewBinding.settingsIgnoredLocTvName.setText(location.LOCATION_NAME)
    }
}