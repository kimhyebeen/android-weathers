package com.khb.weatheralarm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.khb.weatheralarm.model.WeatherModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    var location: Location? = null
    var latitute: Double? = null
    var longitute: Double? = null
    var LOCATION_REQUEST_CODE = 200
    val API_KEY = "7a1407abea3d6cbceb15cfb01da233a2"
    lateinit var networkHelper: NetworkHelper

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) { // permission denied
                println("거절")
                Toast.makeText(this, "권한이 거절되어 정상적인 사용이 어려움", Toast.LENGTH_SHORT).show()
                return
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitute = location?.latitude
            longitute = location?.longitude
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkHelper = NetworkHelper()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestPermissions()

        val param = mapOf(
            "lat" to latitute.toString(),
            "lon" to latitute.toString(),
            "exclude" to "{current,minutely,daily}",
            "appid" to API_KEY,
            "units" to "metric"
        )
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkHelper.setRetrofit(retrofit)
        networkHelper.callWeatherList(param)

    }

    fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // request permissions
            println("여기는????")
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_REQUEST_CODE
            )
        } else { // get data
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitute = location?.latitude
            longitute = location?.longitude
        }
    }
}