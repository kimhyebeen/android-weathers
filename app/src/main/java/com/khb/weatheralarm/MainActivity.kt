package com.khb.weatheralarm

import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.khb.weatheralarm.helper.LocationHelper
import com.khb.weatheralarm.helper.NetworkHelper
import com.khb.weatheralarm.model.WeatherAPI
import com.khb.weatheralarm.model.WeatherModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    lateinit var networkHelper: NetworkHelper
    var currentWeatherModel: WeatherModel? = null
//    var hourlyWeatherModel: WeatherModel? = null
    var location: Location? = null
    val LOCATION_REQUEST_CODE = 200

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            location = locationHelper.locationPermissionResult()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationHelper = LocationHelper(this)
        networkHelper = NetworkHelper(this)

        refreshAll()

    }

    fun refreshAll() {
        GlobalScope.launch {
            // get the location
            withContext(Dispatchers.Default) {
                location = locationHelper.requestLocationPermissions()
            }

            // get the weather api
            location?.let {
                // 원래는 파라미터에 it.latitude.toString()이랑 it.longitude.toString()을 넣어줘야 함.
                networkHelper.requestCurrentWeatherAPI("37.305443", "126.817403")
                    ?.enqueue(object : Callback<WeatherModel> {
                        override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                            println("2 실패 : $t")
                        }

                        override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                            println("2 성공 : ${response.body().toString()}")
                            response.body()?.let { loadCurrentData(it) }
                        }
                    })
            } ?: launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "There is no location information.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCurrentData(weather: WeatherModel) {
        currentTempTextView.text = "${weather.current.temp.toInt()} ℃"
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${weather.current.weather[0].icon}@2x.png")
            .into(mainWeatherImageView)
    }

    private fun loadHourlyData(weather: WeatherModel) {

    }
}