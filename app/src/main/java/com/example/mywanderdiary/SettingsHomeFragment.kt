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
import com.example.mywanderdiary.database.Database
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class SettingsHomeFragment : Fragment(R.layout.fragment_settings_home), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private lateinit var locationDatabase: Database
    private lateinit var homeInput: EditText
    private lateinit var saveBtn: Button
    private var lat = 0.0
    private var lon = 0.0


    // Initialize locationDatabase in onAttach instead of onViewCreated
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to initialize now that the fragment is attached to the context
        locationDatabase = Database(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings_home, container, false)

        // Initialize the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.fragment_settings_home_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        homeInput = view.findViewById(R.id.fragment_settings_home_et_address)
        saveBtn = view.findViewById(R.id.fragment_settings_home_btn_save)
        homeInput.setText(Database.cachedLocations.get(0).LOCATION_NAME)

        saveBtn.setOnClickListener(){
            findOnMap()

            locationDatabase.updateLocation(
                Location(
                    0,
                    homeInput.text.toString(),
                    LocationType.HOME,
                    lat,
                    lon
                )
            )

            Toast.makeText(requireContext(), "Updated Home Location", Toast.LENGTH_SHORT).show()

        }
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = Database.cachedLocations.get(0).let {
            LatLng(
                it.LOCATION_LAT,
                it.LOCATION_LON
            )
        }
        defaultLocation.let { MarkerOptions().position(it).title("Home") }
            .let { gMap.addMarker(it) }
        defaultLocation.let { CameraUpdateFactory.newLatLngZoom(it, 14f) }
            .let { gMap.moveCamera(it) }
    }

    private fun findOnMap() {
        val addressText = homeInput.text.toString()
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

    private fun goToLocation(latitude: Double, longitude: Double, zoom: Float) {
        val newLocation = LatLng(latitude, longitude)
        gMap.clear() // Clear existing markers
        gMap.addMarker(MarkerOptions().position(newLocation).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, zoom))
    }
}