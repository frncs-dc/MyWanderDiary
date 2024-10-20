package com.example.mywanderdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class LandingPageFragment: Fragment(R.layout.activity_landing_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.activity_landing_page_btn_getstarted).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(HomeLocationFragment())
        }
    }
}