package com.example.mywanderdiary

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.database.LocationDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class CustomIgnoredLocFragment: Fragment(R.layout.activity_start_custom_ignored), OnMapReadyCallback
{
    private lateinit var gMap: GoogleMap
    private lateinit var addressInput: EditText
    private var lat = 14.562961;
    private var lon = -239.005310;
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var addMoreAddressButton: Button
    private lateinit var continueButton: Button
    private lateinit var searchButton: Button

    // Initialize locationDatabase in onAttach instead of onViewCreated
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to initialize now that the fragment is attached to the context
        locationDatabase = LocationDatabase(context)
        locationDatabase.initializeCachedLocations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize address input
        addressInput = view.findViewById(R.id.activity_start_custom_ignored_et_address)
        addMoreAddressButton = view.findViewById(R.id.activity_start_custom_ignored_btn_addmore)
        continueButton = view.findViewById(R.id.activity_start_custom_ignored_btn_continue)
        searchButton = view.findViewById(R.id.activity_onboarding_set_custom_btn_search)

        // Initialize the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.activity_onboarding_set_custom_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        searchButton.setOnClickListener(){
            findOnMap()
        }

        addMoreAddressButton.setOnClickListener(){
            findOnMap()
            locationDatabase.addLocation(
                Location(
                    (LocationDatabase.cachedLocations?.size ?: 0) + 1,
                    addressInput.text.toString(),
                    LocationType.IGNORED,
                    lat,
                    lon
                )
            )

            addressInput.text.clear()
            Toast.makeText(requireContext(), "Location added", Toast.LENGTH_SHORT).show()

        }

        continueButton.setOnClickListener {
            val addressText = addressInput.text.toString()
            if (addressText.isEmpty()) {
                (activity as OnboardingActivity).setCurrentFragment(AllSetFragment())
            } else {
                Toast.makeText(requireContext(), "Please add the address first before continuing",
                    Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = LatLng(lat, lon)
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
            val addressList: List<Address>? = geocoder.getFromLocationName(addressText, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val locality = address.locality ?: "Unknown Location"
                lat = address.latitude
                lon = address.longitude

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