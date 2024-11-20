package com.example.mywanderdiary

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}