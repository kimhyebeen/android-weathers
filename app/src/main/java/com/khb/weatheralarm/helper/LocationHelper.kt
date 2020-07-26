package com.khb.weatheralarm.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

class LocationHelper(context: Context) {
    private val context: Context
    private var locationManager: LocationManager
    val LOCATION_REQUEST_CODE = 200
    var location: Location? = null

    init {
        this.context = context
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun getLocationManager(): LocationManager {
        return this.locationManager
    }

    fun locationPermissionResult(): Location? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // permission denied
            println("거절")
            Toast.makeText(context, "권한이 거절되어 정상적인 사용이 어려움", Toast.LENGTH_SHORT).show()
            return null
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return location
    }

    fun requestLocationPermissions(): Location? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // request permissions
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_REQUEST_CODE
            )
        } else { // get data
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        return location
    }
}