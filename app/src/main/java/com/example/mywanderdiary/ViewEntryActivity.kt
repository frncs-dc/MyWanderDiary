package com.example.mywanderdiary

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.whenStarted
import com.example.mywanderdiary.database.Database
import com.example.mywanderdiary.databinding.ActivityViewEntryBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.io.IOException

class ViewEntryActivity : AppCompatActivity(), DeleteDialogFragment.DeleteDialogListener,
    OnMapReadyCallback {

    private lateinit var binding: ActivityViewEntryBinding
    private lateinit var gMap: GoogleMap
    private var lat = 14.562961;
    private var lon = -239.005310;
    private lateinit var locationDatabase: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val entry = intent.getSerializableExtra("KEY_ENTRY") as Entry

        locationDatabase = Database(this)
        locationDatabase.initializeCachedLocations()

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.activity_view_entry_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        findOnMap(entry)

        Picasso.get()
            .load(entry.imageId)
            .into(this.binding.activityViewEntryCoverImage)

        Log.d("IMAGE ID", entry.imageId)
        binding.activityViewEntryLocation.text = entry.entryName
        binding.activityViewEntryDateCountry.text = entry.stringDate + " - " + entry.countryName
        binding.activityViewEntryContentText.text = entry.entryContent

        binding.activityViewEntryBtnClose.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        binding.activityViewEntryBtnEdit.setOnClickListener{
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("KEY_ENTRY", entry)
            startActivity(intent)
        }

        binding.activityViewEntryBtnDelete.setOnClickListener{
            DeleteDialogFragment().show(supportFragmentManager, "DELETE_DIALOG")
        }
    }

    override fun onDeleteConfirmed() {
        Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        // TODO: Change to current location
        val defaultLocation = LatLng(lat, lon)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
    }

    private fun findOnMap(entry: Entry) {
        val location = locationDatabase.getLocationThruID(entry.locationID)
        val addressText = location?.LOCATION_NAME
        if (addressText != null) {
            if (addressText.isEmpty()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (location != null) {
            goToLocation(location.LOCATION_LAT, location.LOCATION_LON, 14f)
        }
    }

    private fun goToLocation(latitude: Double, longitude: Double, zoom: Float) {
        val newLocation = LatLng(latitude, longitude)
        gMap.clear() // Clear existing markers
        gMap.addMarker(MarkerOptions().position(newLocation).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, zoom))
    }

}