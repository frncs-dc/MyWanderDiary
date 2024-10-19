package com.example.mywanderdiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.databinding.ActivityMainBinding
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the ViewPager with the TabLayout
        val fragmentList = listOf(
            SettingsHomeFragment(),
            SettingsAwayFragment(),
            SettingsCustomFragment()
        )

        // You can set the initial fragment with bottom navigation as well
        val homeFragment = HomeFragment()
        val mapFragment = MapFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_home -> setCurrentFragment(homeFragment)
                R.id.menu_item_map -> setCurrentFragment(mapFragment)
                R.id.menu_item_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }

        binding.mainActivityBtnAddEntry.setOnClickListener{
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
            if(fragment is SettingsFragment) {
                binding.mainActivityBtnAddEntry.visibility = View.GONE
            } else {
                binding.mainActivityBtnAddEntry.visibility = View.VISIBLE
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