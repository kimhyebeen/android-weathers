package com.khb.weatheralarm

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.khb.weatheralarm.list_item.DailyTable
import com.khb.weatheralarm.list_item.HourlyTable
import com.khb.weatheralarm.api_model.MainApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    lateinit var networkHelper: NetworkHelper

    var location: Location? = null
    val LOCATION_REQUEST_CODE = 200
    val DARK_COLOR = Color.parseColor("#505050")

    // @SuppressLint("NewApi")는 해당 프로젝트의 설정 된 minSdkVersion 이후에 나온 API를 사용할때  warning을 없애고 개발자가 해당 APi를 사용할 수 있게 합니다.
    @SuppressLint("SimpleDateFormat")
    var dailyDateFormat = SimpleDateFormat("MM/dd")
    @SuppressLint("SimpleDateFormat")
    var hourlyTimeFormat = SimpleDateFormat("HH시")

    var hourlyWeatherAdapter = HourlyWeatherAdapter()
    var dailyWeatherAdapter = DailyWeatherAdapter()

    lateinit var setHourlyData : (MainApi) -> Unit
    lateinit var setCurrentData : (MainApi) -> Unit
    lateinit var setDailyData : (MainApi) -> Unit

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

        settingApiData()

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
            Toast.makeText(this, "새로고침 되었습니다", Toast.LENGTH_SHORT).show()
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
                    networkHelper.requestCurrentWeatherAPI("37.305443", "126.817403", setCurrentData)
                }
                launch(Dispatchers.IO) { // hourly
                    networkHelper.requestHourlyWeatherAPI("37.305443", "126.817403", setHourlyData)
                }
                launch(Dispatchers.IO) { // daily
                    networkHelper.requestDailyWeatherAPI("37.305443", "126.817403", setDailyData)
                }
            } ?: launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "There is no location information.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun settingApiData() {
        setHourlyData = { model ->
            println("hourly 실행")
            while(hourlyWeatherAdapter.itemCount>0) hourlyWeatherAdapter.removeItem(0)
            // 시간별 날씨 recycler view 적용
            for (i in 0..23) {
                model.hourly?.get(i)?.let { item ->
                    hourlyWeatherAdapter.addItem(
                        HourlyTable(
                            hourlyTimeFormat.format(item.dt!! * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp).toInt()}${getString(R.string.celsius)}"
                        )
                    )
                }
            }
        }

        setCurrentData = { model ->
            println("current 실행")
            model.current!!.weather[0].id.let {
                if (it>800) mainConstraintLayout.background = getDrawable(R.drawable.bg_clouds)
                else if (it==800) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_clear)
                    colorToDart()
                } else if (it>=700) mainConstraintLayout.background = getDrawable(R.drawable.bg_atmosphere)
                else if (it>=600) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_snow)
                    colorToDart()
                } else if (it>=300) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_rain)
                    colorToDart()
                } else mainConstraintLayout.background = getDrawable(R.drawable.bg_storm)
            }

            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${model.current!!.weather[0].icon}@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(mainWeatherImageView)
            currentTempTextView.text = "${model.current!!.temp.toInt()}"
        }

        setDailyData = { model ->
            println("daily 실행")
            model.daily?.get(0)?.let { item ->
                maxminTempTextView.text = "${(item.temp.max).toInt()}${getString(R.string.celsius)} / ${(item.temp.min).toInt()}${getString(R.string.celsius)}"
            }
            while(dailyWeatherAdapter.itemCount>0) dailyWeatherAdapter.removeItem(0)
            for (i in 1..7) {
                model.daily?.get(i)?.let { item ->
                    dailyWeatherAdapter.addItem(
                        DailyTable(
                            dailyDateFormat.format(item.dt!! * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp.max).toInt()}",
                            "${(item.temp.min).toInt()}"
                        )
                    )
                }
            }
        }
    }

    fun colorToDart() {
        currentTempTextView.setTextColor(DARK_COLOR)
        celsiusTextView.setTextColor(DARK_COLOR)
        tempText.setTextColor(DARK_COLOR)
        maxminTempTextView.setTextColor(DARK_COLOR)
    }
}