package com.example.mywanderdiary

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SettingsHomeFragment()
            1 -> SettingsAwayFragment()
            else -> SettingsCustomFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}