package com.example.mywanderdiary

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO) // bound to lifetime of our service
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "This channel is used by MyWanderDiary to track your location."
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() { // to start foreground service of location tracking
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("MyWanderDiary tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this drawable exists
            .setOngoing(true)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L) // 20 seconds interval of getting new location
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())

                // Check location against conditions
                // if (isAwayFromHome(homeLatitude, homeLongitude, location.latitude.toDouble(),location.longitude.toDouble(), awayRadius) && !isIgnoredLocation(location)) {
                    // Trigger notification for new location
                //    sendLocationDetectedNotification()
                // }
            }
            .launchIn(serviceScope)

        Log.d("LOC:", "service start")

        startForeground(1, notification.build())

        // TODO: Add functionality where:
    //      if user's current location != home location
        //  && user outside of away radius
        //  && user's current location != list of ignored locations
        //      then notify user "New location detected! Make entry?"
    }

    private fun stop() {
        stopForeground(true) // to remove notification
        stopSelf() // to stop service
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    /*
    private fun isAwayFromHome(homeLatitude: Double, homeLongitude: Double, currentLatitude: Double, currentLongitude: Double, awayRadius: Int): Boolean {
        // Convert latitude and longitude differences to km
        val latDiff = (currentLatitude - homeLatitude) * 110.574
        val longDiff = (currentLongitude - homeLongitude) * 111.320 * Math.cos(Math.toRadians(homeLatitude))

        // Calculate the total distance in km
        val distance = Math.sqrt(latDiff * latDiff + longDiff * longDiff)

        // Check if the distance exceeds the away radius (in km)
        return distance > awayRadius
    }

    private fun isIgnoredLocation(location: Location): Boolean {
        return ignoredLocations.any {
            it.distanceTo(location) < 100f
        }
    }

    private fun sendLocationDetectedNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("New location detected!")
            .setContentText("Make a new entry in MyWanderDiary?")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .build()

        notificationManager.notify(2, notification)
    }

     */

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}