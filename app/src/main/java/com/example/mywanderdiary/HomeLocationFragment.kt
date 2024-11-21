package com.example.mywanderdiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeLocationFragment: Fragment(R.layout.activity_start_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Intent(this.context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            context?.startService(this)
        }

        view.findViewById<Button>(R.id.activity_start_home_btn_continue).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(SetRadiusFragment())
        }
    }
}