package com.example.mywanderdiary

import androidx.viewbinding.ViewBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.ActivityMainEntryBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso

class EntryViewHolder (private val viewBinding: ActivityMainEntryBinding): RecyclerView.ViewHolder(viewBinding.root){
    fun bindData(entry: Entry){
        Picasso.get()
            .load(entry.imageId)
            .into(this.viewBinding.activityMainEntryIvImage)
        this.viewBinding.activityMainEntryTvDate.text = entry.stringDate
        this.viewBinding.activityMainEntryTvCountry.text = " - " + entry.countryName
        this.viewBinding.activityMainEntryTvLocation.text = entry.entryName
    }
}