package com.example.mywanderdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class CustomIgnoredLocFragment: Fragment(R.layout.activity_start_custom_ignored) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.activity_start_custom_ignored_btn_continue).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(AllSetFragment())
        }
    }
}