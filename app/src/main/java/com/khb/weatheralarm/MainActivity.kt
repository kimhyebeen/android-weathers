package com.khb.weatheralarm

import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.khb.weatheralarm.helper.LocationHelper
import com.khb.weatheralarm.model.WeatherAPI
import com.khb.weatheralarm.model.WeatherModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    var location: Location? = null
    var latitute: Double? = null
    var longitute: Double? = null
    val LOCATION_REQUEST_CODE = 200

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            location = locationHelper.locationPermissionResult()
            location?.let {
                latitute = it.latitude
                longitute = it.longitude
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationHelper = LocationHelper(this)

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                location = locationHelper.requestLocationPermissions()
                location?.let {
                    latitute = it.latitude
                    longitute = it.longitude
                }
            }
            launch(Dispatchers.Main) {
                testTextView.text = "($latitute, $longitute)"
                println("3. latitute: $latitute, longitute: $longitute")
            }
            launch(Dispatchers.IO) {
                requestWeatherAPI()
                println("5. latitute: $latitute, longitute: $longitute")
            }
        }

    }

    private fun requestWeatherAPI() {
        // 네트워크 작업
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(WeatherAPI::class.java)
        val callGetWeather = api.getWeatherList(latitute!!.toString(), longitute!!.toString(), "{current,minutely,daily}", getString(R.string.api_key), "metric")

        callGetWeather.enqueue(object : Callback<WeatherModel> {
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                println("실패 : $t")
            }

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                println("성공 : ${response.body().toString()}")
            }

        })
    }
}