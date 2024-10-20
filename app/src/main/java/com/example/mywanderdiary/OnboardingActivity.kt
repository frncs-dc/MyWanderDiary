package com.example.mywanderdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.databinding.ActivityLandingPageBinding
import com.example.mywanderdiary.databinding.ActivityOnboardingBinding
import com.example.mywanderdiary.databinding.ActivityStartAllsetBinding
import com.example.mywanderdiary.databinding.ActivityStartAwayRadiusBinding
import com.example.mywanderdiary.databinding.ActivityStartCustomIgnoredBinding
import com.example.mywanderdiary.databinding.ActivityStartHomeBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var landingpageFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        landingpageFragment = LandingPageFragment()

        setCurrentFragment(landingpageFragment)

    }

    fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_onboarding_container, fragment)
            commit()
        }

}