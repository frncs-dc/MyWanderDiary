package com.example.mywanderdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var tabLayout: TabLayout;
    private lateinit var viewPager2: ViewPager2;
    private lateinit var adapter: ViewPagerAdapter;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        tabLayout = view.findViewById(R.id.fragment_settings_tabLayout)
        viewPager2 = view.findViewById(R.id.fragment_settings_viewPager)
        adapter = ViewPagerAdapter(this)

        viewPager2.adapter = adapter

        viewPager2.isUserInputEnabled = false

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null) {
                    viewPager2.currentItem = tab!!.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val position = tab.position
                    val previousFragment = adapter.createFragment(position)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val position = tab.position
                    val currentFragment = adapter.createFragment(position) // Get the current fragment
                }
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return view
    }

}