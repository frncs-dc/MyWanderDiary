package com.example.mywanderdiary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mywanderdiary.database.Database
import com.example.mywanderdiary.databinding.ActivityAddEntryBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class AddEntryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddEntryBinding

    private lateinit var gMap: GoogleMap
    private var lat = 14.562961;
    private var lon = -239.005310;
    private lateinit var locationDatabase: Database

    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var location: Location;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationDatabase = Database(this)
        locationDatabase.initializeCachedLocations()

        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.add_entry_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.addImgBtn.setOnClickListener(){
            openGallery()
        }

        binding.searchLocBtn.setOnClickListener(){
            findOnMap()
        }

        binding.acitivityBtnAddEntry.setOnClickListener {
            val entryName = binding.activityAddEntryInputLocation.text.toString()
            val visitDateString = binding.activityAddEntryInputDate.text.toString()
            val journalEntry = binding.activityAddEntryInputContent.text.toString()

            // get location details of location
            val locationDetails = getLocationDetails(this, lat, lon)

            // Parse the location details
            val parsedDetails = parseLocationDetails(locationDetails)

            location = parsedDetails["addressLine"]?.let { it1 ->
                Location(
                    Database.cachedLocations.size + 1,
                    it1,
                    LocationType.JOURNAL,
                    lat,
                    lon
                )
            }!!


            val locationID = locationDatabase.addLocation(
                location
            )

            // Define the date format based on the input format
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val visitDate = dateFormat.parse(visitDateString)
            val addressLine = parsedDetails["addressLine"] ?: "Unknown address line"
            val city = parsedDetails["city"] ?: "Unknown city"
            val state = parsedDetails["state"] ?: "Unknown state"
            val country = parsedDetails["country"] ?: "Unknown country"

            // make entry with locationID
            val entry =
                Entry(entryName, visitDateString, journalEntry, country, imageUri.toString(), locationID)
            locationDatabase.addEntry(entry)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.acitivityAddEntryClose.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    fun parseLocationDetails(locationDetails: String): Map<String, String> {
        // Split the location details string by commas
        val parts = locationDetails.split(",").map { it.trim() }

        // Create a map to store the parsed components
        val parsedDetails = mutableMapOf<String, String>()

        // Check if the parts array has 4 or more elements (addressLine, city, state, country)
        if (parts.size >= 4) {
            parsedDetails["addressLine"] = parts[0]
            parsedDetails["city"] = parts[1]
            parsedDetails["state"] = parts[2]
            parsedDetails["country"] = parts[3]
        } else {
            // Handle case when there are not enough parts (could be less than 4)
            parsedDetails["error"] = "Invalid location format"
        }

        return parsedDetails
    }

    fun getLocationDetails(context: Context, latitude: Double, longitude: Double): String {
        // Check if Geocoder is available on the device
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            // Get the list of addresses based on latitude and longitude
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            // If addresses is not null and has at least one entry
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressLine = address.getAddressLine(0)
                val city = address.locality
                val state = address.adminArea
                val country = address.countryName

                // Combine the details into a single string or format as needed
                return "$addressLine, $city, $state, $country"
            } else {
                return "No address found for the given coordinates"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error occurred while fetching location details"
        }
    }

    private fun openGallery() {
        // Intent to pick an image from the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                // Copy content to cache and get a local URI
                val localUri = copyUriToCache(selectedImageUri)
                if (localUri != null) {
                    binding.imagePreview.setImageURI(localUri)
                    imageUri = localUri
                } else {
                    Log.e("ImageCopy", "Failed to copy selected image to cache.")
                }
            }
        }
    }

    private fun copyUriToCache(uri: Uri): Uri? {
        return try {
            // Create a file in the cache directory
            val cacheFile = File(cacheDir, "cached_image_${System.currentTimeMillis()}.jpg")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                cacheFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            // Return a file URI
            Uri.fromFile(cacheFile)
        } catch (e: Exception) {
            Log.e("ImageCopy", "Failed to copy URI to cache", e)
            null
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Default location
        // TODO: Change to current location
        val defaultLocation = LatLng(lat, lon)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Home"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
    }

    private fun findOnMap() {
        val addressText = binding.inputLocTxt.text.toString()
        if (addressText.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            return
        }

        val geocoder = Geocoder(this)
        try {
            val addressList: List<Address>? = geocoder.getFromLocationName(addressText, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val locality = address.locality ?: "Unknown Location"
                lat = address.latitude
                lon = address.longitude

                Toast.makeText(this, "Found: $locality", Toast.LENGTH_SHORT).show()
                goToLocation(lat, lon, 14f)
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Log.e("HomeLocationFragment", "Geocoding failed", e)
            Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToLocation(latitude: Double, longitude: Double, zoom: Float) {
        val newLocation = LatLng(latitude, longitude)
        gMap.clear() // Clear existing markers
        gMap.addMarker(MarkerOptions().position(newLocation).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, zoom))
    }
}