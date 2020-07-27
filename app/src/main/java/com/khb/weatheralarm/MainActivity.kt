package com.khb.weatheralarm

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.khb.weatheralarm.helper.LocationHelper
import com.khb.weatheralarm.helper.NetworkHelper
import com.khb.weatheralarm.itemview.DailyWeatherItems
import com.khb.weatheralarm.model.WeatherAPI
import com.khb.weatheralarm.model.WeatherModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    lateinit var networkHelper: NetworkHelper
    var location: Location? = null
    val LOCATION_REQUEST_CODE = 200
    var simpleDateFormat = SimpleDateFormat("MM/dd")

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
                        ?.enqueue(object : Callback<WeatherModel> {
                            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                                println("current 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                                println("current 성공 : ${response.body().toString()}")
                                response.body()?.let { loadCurrentData(it) }
                            }
                        })
                }
                launch(Dispatchers.IO) {
                    networkHelper.requestHourlyWeatherAPI("37.305443", "126.817403")
                        ?.enqueue(object : Callback<WeatherModel> {
                            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                                println("hourly 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                                println("hourly 성공 : ${response.body().toString()}")
                                response.body()?.let { loadHourlyData(it) }
                            }
                        })
                }
                launch(Dispatchers.IO) {
                    networkHelper.requestDailyWeatherAPI("37.305443", "126.817403")
                        ?.enqueue(object : Callback<WeatherModel> {
                            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                                println("daily 실패 : $t")
                            }

                            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
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

    private fun loadCurrentData(weather: WeatherModel) {
        println("current 실행")
        weather.current!!.weather[0].id.let {
            if (it>800) mainConstraintLayout.background = getDrawable(R.drawable.bg_clouds)
            else if (it==800) mainConstraintLayout.background = getDrawable(R.drawable.bg_clear)
            else if (it>=700) mainConstraintLayout.background = getDrawable(R.drawable.bg_atmosphere)
            else if (it>=600) mainConstraintLayout.background = getDrawable(R.drawable.bg_snow)
            else if (it>=300) mainConstraintLayout.background = getDrawable(R.drawable.bg_rain)
            else mainConstraintLayout.background = getDrawable(R.drawable.bg_storm)
        }
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${weather.current!!.weather[0].icon}@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
            .into(mainWeatherImageView)
        currentTempTextView.text = "${weather.current!!.temp.toInt()}"
    }

    private fun loadHourlyData(weather: WeatherModel) {
        println("hourly 실행")
    }

    @SuppressLint("SetTextI18n")
    private fun loadDailyData(weather: WeatherModel) {
        println("daily 실행")
        maxminTempTextView.text = "${(weather.daily?.get(0)?.temp?.max)?.toInt()}${getString(R.string.celsius)} / ${(weather.daily?.get(0)?.temp?.min)?.toInt()}${getString(R.string.celsius)}"
        // daily table에 daily item view 추가
        dailyWeatherItemLinear.removeAllViews()
        for (i in 0..6) {
            var dailyWeatherItems = DailyWeatherItems(this)
            var dateTextView = dailyWeatherItems.findViewById<TextView>(R.id.dailyItemDateTextView)
            var dailyImageItem = dailyWeatherItems.findViewById<ImageView>(R.id.dailyItemImageView)
            var dailyMinMaxTemp = dailyWeatherItems.findViewById<TextView>(R.id.dailyItemMaxMinTempTextView)
            dateTextView.text = "${weather.daily?.get(i)?.dt?.let { simpleDateFormat.format(Date(it*1000L)) }}"
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${weather.daily?.get(i)?.weather?.get(0)?.icon}@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(dailyImageItem)
            dailyMinMaxTemp.text = "${(weather.daily?.get(i)?.temp?.day)?.toInt()}${getString(R.string.celsius)}"
            dailyWeatherItemLinear.addView(dailyWeatherItems)
        }
    }
}