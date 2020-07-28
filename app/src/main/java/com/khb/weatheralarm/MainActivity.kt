package com.khb.weatheralarm

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.khb.weatheralarm.adapter.DailyWeatherAdapter
import com.khb.weatheralarm.adapter.HourlyWeatherAdapter
import com.khb.weatheralarm.helper.LocationHelper
import com.khb.weatheralarm.helper.NetworkHelper
import com.khb.weatheralarm.model.DailyTableItem
import com.khb.weatheralarm.model.HourlyTableItem
import com.khb.weatheralarm.model.WeatherApiModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    lateinit var networkHelper: NetworkHelper
    var location: Location? = null
    val LOCATION_REQUEST_CODE = 200
    var dailyDateFormat = SimpleDateFormat("MM/dd")
    var hourlyTimeFormat = SimpleDateFormat("HH시")

    var hourlyWeatherAdapter = HourlyWeatherAdapter()
    var dailyWeatherAdapter = DailyWeatherAdapter()

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

        hourlyWeatherRecyclerView.adapter = hourlyWeatherAdapter
        hourlyWeatherRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourlyWeatherRecyclerView.addItemDecoration(DividerItemDecoration(this, 0))

        dailyWeatherRecyclerView.adapter = dailyWeatherAdapter
        dailyWeatherRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        refreshApi()

        refreshButton.setOnClickListener {
            refreshApi()
        }
    }

    fun refreshApi() {
        GlobalScope.launch {
            // get the location
            withContext(Dispatchers.Default) {
                location = locationHelper.requestLocationPermissions()
            }

            // get the weather api
            location?.let {
                launch(Dispatchers.IO) { // current
                    // 원래는 파라미터에 it.latitude.toString()이랑 it.longitude.toString()을 넣어줘야 함.
                    networkHelper.requestCurrentWeatherAPI("37.305443", "126.817403")
                        ?.enqueue(object : Callback<WeatherApiModel> {
                            override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                                println("current 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                                println("current 성공 : ${response.body().toString()}")
                                response.body()?.let { loadCurrentData(it) }
                            }
                        })
                }
                launch(Dispatchers.IO) {
                    networkHelper.requestHourlyWeatherAPI("37.305443", "126.817403")
                        ?.enqueue(object : Callback<WeatherApiModel> {
                            override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                                println("hourly 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                                println("hourly 성공 : ${response.body().toString()}")
                                response.body()?.let { loadHourlyData(it) }
                            }
                        })
                }
                launch(Dispatchers.IO) {
                    networkHelper.requestDailyWeatherAPI("37.305443", "126.817403")
                        ?.enqueue(object : Callback<WeatherApiModel> {
                            override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                                println("daily 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                                println("daily 성공 : ${response.body().toString()}")
                                response.body()?.let { loadDailyData(it) }
                            }
                        })
                }
            } ?: launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "There is no location information.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCurrentData(weatherApi: WeatherApiModel) {
        println("current 실행")
        weatherApi.current!!.weather[0].id.let {
            if (it>800) mainConstraintLayout.background = getDrawable(R.drawable.bg_clouds)
            else if (it==800) mainConstraintLayout.background = getDrawable(R.drawable.bg_clear)
            else if (it>=700) mainConstraintLayout.background = getDrawable(R.drawable.bg_atmosphere)
            else if (it>=600) mainConstraintLayout.background = getDrawable(R.drawable.bg_snow)
            else if (it>=300) mainConstraintLayout.background = getDrawable(R.drawable.bg_rain)
            else mainConstraintLayout.background = getDrawable(R.drawable.bg_storm)
        }
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${weatherApi.current!!.weather[0].icon}@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
            .into(mainWeatherImageView)
        currentTempTextView.text = "${weatherApi.current!!.temp.toInt()}"
    }

    private fun loadHourlyData(weatherApi: WeatherApiModel) {
        println("hourly 실행")
        hourlyWeatherRecyclerView.removeAllViews()
        // 시간별 날씨 recycler view 적용
        for (i in 0..23) {
            weatherApi.hourly?.get(i)?.let {
                hourlyWeatherAdapter.addItem(
                    HourlyTableItem(
                        "${hourlyTimeFormat.format(it.dt!!*1000L)}",
                        "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png",
                        "${(it.temp).toInt()}${getString(R.string.celsius)}"
                    )
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDailyData(weatherApi: WeatherApiModel) {
        println("daily 실행")
        weatherApi.daily?.get(0)?.let {
            maxminTempTextView.text = "${(it.temp.max).toInt()}${getString(R.string.celsius)} / ${(it.temp.min).toInt()}${getString(R.string.celsius)}"
        }
        for (i in 1..7) {
            weatherApi.daily?.get(i)?.let {
                dailyWeatherAdapter.addItem(
                    DailyTableItem(
                        "${dailyDateFormat.format(it.dt!!*1000L)}",
                        "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png",
                        "${(it.temp.max).toInt()}",
                        "${(it.temp.min).toInt()}"
                    )
                )
            }
        }
        // 날짜별 날씨 recycler view 적용
    }
}