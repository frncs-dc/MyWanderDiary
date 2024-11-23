package com.example.mywanderdiary

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mywanderdiary.database.DBHandler
import com.example.mywanderdiary.database.LocationDatabase
import com.example.mywanderdiary.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the LocationDatabase instance
        val locationDatabase = LocationDatabase(this)

        // Initialize cachedLocations to load the data from the DB
        locationDatabase.initializeCachedLocations()

        ActivityCompat.requestPermissions( // Requests user for permission
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            0
        )

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val complete_status = intent.getStringExtra("KEY_ONBOARDING_STATUS")

        if(!complete_status.equals("COMPLETE")){
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        } else {
            mainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(mainBinding.root)

            // You can set the initial fragment with bottom navigation as well
            val homeFragment = HomeFragment()
            val mapFragment = MapFragment()
            val settingsFragment = SettingsFragment()

            setCurrentFragment(homeFragment)

            mainBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_item_home -> setCurrentFragment(homeFragment)
                    R.id.menu_item_map -> setCurrentFragment(mapFragment)
                    R.id.menu_item_settings -> setCurrentFragment(settingsFragment)
                }
                true
            }
            mainBinding.mainActivityBtnAddEntry.setOnClickListener{
                val intent = Intent(this, AddEntryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
            if(fragment is SettingsFragment) {
                mainBinding.mainActivityBtnAddEntry.visibility = View.GONE
            } else {
                mainBinding.mainActivityBtnAddEntry.visibility = View.VISIBLE
            }
        }

    // Adapter for ViewPager
    private inner class ViewPagerAdapter(
        activity: AppCompatActivity,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}