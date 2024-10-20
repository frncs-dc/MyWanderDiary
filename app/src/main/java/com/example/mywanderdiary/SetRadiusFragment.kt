package com.example.mywanderdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class SetRadiusFragment: Fragment(R.layout.activity_start_away_radius) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.activity_start_away_radius_btn_continue).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(CustomIgnoredLocFragment())
        }
    }
}