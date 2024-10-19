package com.example.mywanderdiary

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mywanderdiary.databinding.ActivityMainEntryBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding

class EntryAdapter(private val entries: ArrayList<Entry>): RecyclerView.Adapter<EntryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {

        val itemViewBinding: ActivityMainEntryBinding = ActivityMainEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return EntryViewHolder(itemViewBinding)
    }

    // how many items in that itemview
    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bindData(entries.get(position))
        holder.itemView.setOnClickListener(){
            val intent = Intent(holder.itemView.context, ViewEntryActivity::class.java)
            val entry = entries.get(position)
            intent.putExtra("KEY_ENTRY", entry)
            holder.itemView.context.startActivity(intent)
        }
    }
}