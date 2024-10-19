package com.example.mywanderdiary

import androidx.viewbinding.ViewBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.ActivityMainEntryBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding

class EntryViewHolder (private val viewBinding: ActivityMainEntryBinding): RecyclerView.ViewHolder(viewBinding.root){
    fun bindData(entry: Entry){
        this.viewBinding.activityMainEntryIvImage.setImageResource(entry.imageId)
        this.viewBinding.activityMainEntryTvDate.text = entry.date.toString()
        this.viewBinding.activityMainEntryTvCountry.text = entry.countryName
        this.viewBinding.activityMainEntryTvLocation.text = entry.locationName
    }
}