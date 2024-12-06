package com.example.mywanderdiary

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.viewbinding.ViewBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.ActivityMainEntryBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

class EntryViewHolder (private val viewBinding: ActivityMainEntryBinding): RecyclerView.ViewHolder(viewBinding.root){
    fun bindData(entry: Entry){

        Log.d("IMAGE", entry.imageId)
        this.viewBinding.activityMainEntryTvDate.text = entry.stringDate
        this.viewBinding.activityMainEntryTvCountry.text = " - " + entry.countryName
        this.viewBinding.activityMainEntryTvLocation.text = entry.entryName

        val imageUri: Uri = Uri.parse(entry.imageId)

        Picasso.get()
            .load(imageUri)
            .into(viewBinding.activityMainEntryIvImage)
    }
}