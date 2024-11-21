package com.example.mywanderdiary

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class HomeLocationFragment : Fragment(R.layout.activity_start_home), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private lateinit var addressInput: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize address input
        addressInput = view.findViewById(R.id.activity_start_home_et_address)

        // Initialize the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.activity_onboarding_set_home_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Set the button listener
        view.findViewById<Button>(R.id.activity_start_home_btn_continue).setOnClickListener {
            (activity as OnboardingActivity).setCurrentFragment(SetRadiusFragment())
        }

        // Start location service
        Intent(requireContext(), LocationService::class.java).apply {
            action = LocationService.ACTION_START
            requireContext().startService(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = LatLng(14.562961, -239.005310)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
    }

    private fun goToLocation(latitude: Double, longitude: Double, zoom: Float) {
        val newLocation = LatLng(latitude, longitude)
        gMap.clear() // Clear existing markers
        gMap.addMarker(MarkerOptions().position(newLocation).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, zoom))
    }

    private fun findOnMap() {
        val addressText = addressInput.text.toString()

        if (addressText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an address", Toast.LENGTH_SHORT).show()
            return
        }

        val geocoder = Geocoder(requireContext())
        try {
            val addressList: List<Address> = geocoder.getFromLocationName(addressText, 1)
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                val locality = address.locality ?: "Unknown Location"
                val lat = address.latitude
                val lon = address.longitude

                Toast.makeText(requireContext(), "Found: $locality", Toast.LENGTH_SHORT).show()
                goToLocation(lat, lon, 14f)
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Log.e("HomeLocationFragment", "Geocoding failed", e)
            Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show()
        }
    }
}
