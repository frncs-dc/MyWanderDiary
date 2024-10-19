package com.example.mywanderdiary

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.example.mywanderdiary.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // You can set the initial fragment with bottom navigation as well
        val homeFragment = HomeFragment()
        val mapFragment = MapFragment()
        val settingsFragment = SettingsFragment()
        val settingsHomeFragment = SettingsHomeFragment()
        val settingsAwayFragment = SettingsAwayFragment()
        val settingsCustomFragment = SettingsCustomFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_home -> setCurrentFragment(homeFragment)
                R.id.menu_item_map -> setCurrentFragment(mapFragment)
                R.id.menu_item_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}