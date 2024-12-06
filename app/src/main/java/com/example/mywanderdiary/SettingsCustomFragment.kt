package com.example.mywanderdiary

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywanderdiary.database.Database
import com.example.mywanderdiary.databinding.FragmentSettingsCustomBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class SettingsCustomFragment : Fragment(R.layout.fragment_settings_custom), OnMapReadyCallback {
    private lateinit var homeBinding: FragmentSettingsCustomBinding;
    private lateinit var gMap: GoogleMap
    private lateinit var addressInput: EditText
    private var lat = 14.562961;
    private var lon = -239.005310;
    private lateinit var locationDatabase: Database
    private lateinit var addAddressButton: Button

    // Initialize locationDatabase in onAttach instead of onViewCreated
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to initialize now that the fragment is attached to the context
        locationDatabase = Database(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and set up binding
        homeBinding = FragmentSettingsCustomBinding.inflate(inflater, container, false)
        val rootView = homeBinding.root

        // Initialize RecyclerView for displaying locations
        homeBinding.rvIgnored.adapter = LocationAdapter(Database.cachedLocations)
        homeBinding.rvIgnored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Initialize address input and button
        addressInput = homeBinding.fragmentSettingsCustomEtAddress
        addAddressButton = homeBinding.fragmentSettingsCustomBtnAdd

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_settings_custom_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Set up button click listener
        addAddressButton.setOnClickListener {
            findOnMap()
            // Add location to the database
            locationDatabase.addLocation(
                Location(
                    Database.cachedLocations.size + 1,
                    addressInput.text.toString(),
                    LocationType.IGNORED,
                    lat,
                    lon
                )
            )

            // Clear input field and notify user
            addressInput.text.clear()
            Toast.makeText(requireContext(), "Location added", Toast.LENGTH_SHORT).show()
        }

        return homeBinding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = LatLng(lat, lon)

        for (location in Database.cachedLocations) {
            if (location.LOCATION_TYPE == LocationType.IGNORED) {
                val latLng = LatLng(location.LOCATION_LAT, location.LOCATION_LON)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Ignored - " + location.LOCATION_NAME)
                )
            }
        }

        // gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
    }

    private fun goToLocation(latitude: Double, longitude: Double, zoom: Float) {
        val newLocation = LatLng(latitude, longitude)
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

    // TODO: ADD CARD CLICK functionality to each part of recyclerview
    //       to lead to specific location's pin


}