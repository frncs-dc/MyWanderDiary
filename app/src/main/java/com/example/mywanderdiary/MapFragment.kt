package com.example.mywanderdiary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.database.Database
import com.example.mywanderdiary.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var homeBinding: FragmentMapBinding;
    private lateinit var gMap: GoogleMap
    private var lat = 14.562961;
    private var lon = -239.005310;
    private lateinit var locationDatabase: Database
    private lateinit var addButton: Button

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
        homeBinding = FragmentMapBinding.inflate(inflater, container, false)
        val rootView = homeBinding.root

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return homeBinding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = LatLng(lat, lon)

        for (location in Database.cachedLocations) {
            if (location.LOCATION_TYPE == LocationType.JOURNAL) {
                val latLng = LatLng(location.LOCATION_LAT, location.LOCATION_LON)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Journal - " + location.LOCATION_NAME)
                )
            }
        }

        // gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 11f))
    }
}