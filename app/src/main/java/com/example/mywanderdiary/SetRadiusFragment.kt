package com.example.mywanderdiary

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mywanderdiary.database.Database
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SetRadiusFragment: Fragment(R.layout.activity_start_away_radius), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private lateinit var radiusInput: SeekBar
    private lateinit var seekBarValueTextView: TextView
    private lateinit var locationDatabase: Database
    private var radius = 0;
    private lateinit var location : Location;
    private lateinit var defaultLocation: LatLng;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the fragment is attached before initializing
        if (isAdded) {
            locationDatabase = Database(context)
        }
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location = locationDatabase.getLocations()[0]
        defaultLocation = LatLng(location.LOCATION_LAT, location.LOCATION_LON)

        view.findViewById<Button>(R.id.activity_start_away_radius_btn_continue).setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("WanderDiaryPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("RADIUS", radius)
            editor.apply()
            (activity as OnboardingActivity).setCurrentFragment(CustomIgnoredLocFragment())
        }

        // TODO: Replace addressInput with appropriate value for radius
        radiusInput = view.findViewById(R.id.activity_start_away_radius_seekbar)
        radiusInput.min = 0
        radiusInput.max = 1000

        seekBarValueTextView = view.findViewById(R.id.activity_start_away_radius_tv_seekbar_value)

        // Initialize the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.activity_onboarding_set_away_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        radiusInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Set the text of the TextView to the current progress value
                radius = progress
                seekBarValueTextView.text = progress.toString() + "m"
                updateRadiusCircle(LatLng(location.LOCATION_LAT, location.LOCATION_LON),
                                    radius.toDouble())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: You can handle the start of the touch event here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: You can handle the stop of the touch event here
            }
        })

    }

    // TODO: For this file, change default location to set home location from previous screen
    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
    }

    private fun updateRadiusCircle(center: LatLng, radius: Double){
        // Add a circle to the map
        gMap.clear()
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.addCircle(
            CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(0.5f)
                .strokeColor(0xFFFF0000.toInt()) // Red outline
                .fillColor(0x44FF0000.toInt()) // Transparent red fill
        )

        // Move the camera to the circle's center
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14f))
    }
}