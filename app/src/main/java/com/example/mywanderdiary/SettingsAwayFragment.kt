package com.example.mywanderdiary

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.mywanderdiary.database.LocationDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SettingsAwayFragment : Fragment(R.layout.fragment_settings_away), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var radiusInput: SeekBar
    private lateinit var saveBtn: Button
    private lateinit var seekBarValue: TextView
    private var lat = 0.0
    private var lon = 0.0
    private var radius = 0
    private lateinit var location : Location;
    private lateinit var defaultLocation: LatLng;

    // Initialize locationDatabase in onAttach instead of onViewCreated
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to initialize now that the fragment is attached to the context
        locationDatabase = LocationDatabase(context)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = requireContext().getSharedPreferences("WanderDiaryPrefs", Context.MODE_PRIVATE)
        radius = sharedPreferences.getInt("RADIUS", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings_away, container, false)

        location = locationDatabase.getLocations()[0]
        defaultLocation = LatLng(location.LOCATION_LAT, location.LOCATION_LON)

        // Initialize the map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.fragment_settings_away_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        radiusInput = view.findViewById(R.id.fragment_settings_away_seekbar)
        seekBarValue = view.findViewById(R.id.fragment_settings_away_tv_seekbar_value)
        saveBtn = view.findViewById(R.id.fragment_settings_away_btn_save)

        radiusInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Set the text of the TextView to the current progress value
                radius = progress
                seekBarValue.text = progress.toString() + "m"
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

        saveBtn.setOnClickListener(){
            val sharedPreferences = requireContext().getSharedPreferences("WanderDiaryPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("RADIUS", radius)
            editor.apply()

            Toast.makeText(requireContext(), "Updated Radius" + radius, Toast.LENGTH_SHORT).show()

        }
        return view
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

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        val defaultLocation = LocationDatabase.cachedLocations.get(0).let {
            LatLng(
                it.LOCATION_LAT,
                it.LOCATION_LON
            )
        }
        defaultLocation.let { MarkerOptions().position(it).title("Home") }
            .let { gMap.addMarker(it) }
        defaultLocation.let { CameraUpdateFactory.newLatLngZoom(it, 14f) }
            .let { gMap.moveCamera(it) }

        radiusInput.min = 0
        radiusInput.max = 1000
        radiusInput.progress = radius
        seekBarValue.text = radius.toString() + "m"
    }
}