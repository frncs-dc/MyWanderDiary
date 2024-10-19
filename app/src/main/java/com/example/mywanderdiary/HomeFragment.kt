package com.example.mywanderdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywanderdiary.databinding.ActivityMainBinding
import com.example.mywanderdiary.databinding.FragmentHomeBinding

class HomeFragment:Fragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        homeBinding.rvEntries.adapter = EntryAdapter(DataGeneration.createSampleEntries())
        homeBinding.rvEntries.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return homeBinding.root
    }

}