package com.example.mywanderdiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.databinding.ActivityMainBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.example.mywanderdiary.databinding.ActivityOnboardingBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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