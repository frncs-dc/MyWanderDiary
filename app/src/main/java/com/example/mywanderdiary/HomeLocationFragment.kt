package com.example.mywanderdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class HomeLocationFragment: Fragment(R.layout.activity_start_home), OnMapReadyCallback {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.findViewById<Button>(R.id.activity_start_home_btn_continue).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(SetRadiusFragment())
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }

}