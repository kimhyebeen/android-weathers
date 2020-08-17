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
import com.khb.weatheralarm.api_model.Daily
import com.khb.weatheralarm.api_model.DailyTemp
import com.khb.weatheralarm.api_model.HourlyAndCurrent
import com.khb.weatheralarm.api_model.WeatherOfHourlyAndCurrent
import com.khb.weatheralarm.database_model.CurrentEntity
import com.khb.weatheralarm.database_model.DailyEntity
import com.khb.weatheralarm.database_model.HourlyEntity
import com.khb.weatheralarm.helper.DatabaseHelper
import com.khb.weatheralarm.helper.LocationHelper
import com.khb.weatheralarm.helper.NetworkHelper
import com.khb.weatheralarm.table_model.DailyTable
import com.khb.weatheralarm.table_model.HourlyTable
//import com.khb.weatheralarm.database.WeatherEntity
//import com.khb.weatheralarm.helper.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var locationHelper: LocationHelper
    lateinit var networkHelper: NetworkHelper
    lateinit var databaseHelper: DatabaseHelper

    var location: Location? = null
    val LOCATION_REQUEST_CODE = 200

    var hourlyWeatherAdapter = HourlyWeatherAdapter()
    var dailyWeatherAdapter = DailyWeatherAdapter()

    lateinit var setHourlyData : (ArrayList<HourlyAndCurrent>) -> Unit
    lateinit var setCurrentData : (HourlyAndCurrent) -> Unit
    lateinit var setDailyData : (ArrayList<Daily>) -> Unit

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

        initViewFunctionOfCurrentData()
        initViewFunctionOfHourlyData()
        initViewFunctionOfDailyData()

        locationHelper = LocationHelper(this)
        networkHelper = NetworkHelper(this)

        hourlyWeatherRecyclerView.apply {
            adapter = hourlyWeatherAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerItemDecoration(applicationContext, 0))
        }

        dailyWeatherRecyclerView.apply {
            adapter = dailyWeatherAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

        getDatabaseAndSetView()

        refreshButton.setOnClickListener {
            getWeatherApi()
        }
    }

    private fun getDatabaseAndSetView() {
        GlobalScope.launch {
            databaseHelper = DatabaseHelper.getInstance(applicationContext)

            // TODO(네트워크 연결 안되어 있을 시에만 데이터베이스로 view 세팅하도록 해야 함)
            println("사이즈 : ${databaseHelper.currentDao().getCurrent().size}")
            if (databaseHelper.currentDao().getCurrent().size > 0) {
//                databaseHelper.currentDao().getCurrent().get(0)
//                    .let {
//                        launch(Dispatchers.Main) { setViewFromDatabase(it, databaseHelper.hourlyDao().getHourly(), databaseHelper.dailyDao().getDaily()) }
//                    }
                println("현재 : ${databaseHelper.currentDao().getCurrent()[0]}") // dt = 1597656499
                println("시간별 데이터 개수 : ${databaseHelper.hourlyDao().getHourly().size}개")
                println("날짜별 데이터 개수 : ${databaseHelper.dailyDao().getDaily().size}개")
            }
            println("데이터베이스 작업 끝")
            getWeatherApi()
        }
    }

    private fun getWeatherApi() {
        // TODO(네트워크 연결되어 있을 시에만 동작하도록 해야 함)
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                location = locationHelper.requestLocationPermissions()
            }

            location?.let {
                launch(Dispatchers.IO) { // current
                    networkHelper.requestCurrentWeatherAPI(it.latitude.toString(), it.longitude.toString(), setCurrentData)
                }
                launch(Dispatchers.IO) { // hourly
                    networkHelper.requestHourlyWeatherAPI(it.latitude.toString(), it.longitude.toString(), setHourlyData)
                }
                launch(Dispatchers.IO) { // daily
                    networkHelper.requestDailyWeatherAPI(it.latitude.toString(), it.longitude.toString(), setDailyData)
                }
            } ?: launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "There is no location information.", Toast.LENGTH_SHORT).show()
            }

            refreshButton.isClickable = true
        }
    }

    private fun changeColor(color: Int) {
        currentTempTextView.setTextColor(color)
        celsiusTextView.setTextColor(color)
        tempText.setTextColor(color)
        maxminTempTextView.setTextColor(color)
    }

    private fun initViewFunctionOfCurrentData() {
        val DARK_COLOR = Color.parseColor("#505050")
        val LIGHT_COLOR = Color.parseColor("#eeeeee")

        setCurrentData = { model ->
            println("current 실행")
            model.weather[0].id.let {
                if (it>800) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_clouds)
                    changeColor(LIGHT_COLOR)
                } else if (it==800) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_clear)
                    changeColor(DARK_COLOR)
                } else if (it>=700) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_atmosphere)
                    changeColor(LIGHT_COLOR)
                } else if (it>=600) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_snow)
                    changeColor(DARK_COLOR)
                } else if (it>=300) {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_rain)
                    changeColor(DARK_COLOR)
                } else {
                    mainConstraintLayout.background = getDrawable(R.drawable.bg_storm)
                    changeColor(LIGHT_COLOR)
                }
            }

            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${model.weather[0].icon}@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(mainWeatherImageView)
            currentTempTextView.text = "${model.temp.toInt()}"
        }
    }

    // @SuppressLint("NewApi")는 해당 프로젝트의 설정 된 minSdkVersion 이후에 나온 API를 사용할때  warning을 없애고 개발자가 해당 APi를 사용할 수 있게 합니다.
    @SuppressLint("SimpleDateFormat")
    fun initViewFunctionOfHourlyData() {
        setHourlyData = { model ->
            println("hourly 실행")
            while(hourlyWeatherAdapter.itemCount>0) hourlyWeatherAdapter.removeItem(0)
            // 시간별 날씨 recycler view 적용
            for (i in 0..23) {
                model.get(i)?.let { item ->
                    hourlyWeatherAdapter.addItem(
                        HourlyTable(
                            SimpleDateFormat("HH시").format(item.dt!! * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp).toInt()}${getString(R.string.celsius)}"
                        )
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initViewFunctionOfDailyData() {
        setDailyData = { model ->
            println("daily 실행")
            model.get(0)?.let { item ->
                maxminTempTextView.text = "${(item.temp.max).toInt()}${getString(R.string.celsius)} / ${(item.temp.min).toInt()}${getString(R.string.celsius)}"
            }
            while(dailyWeatherAdapter.itemCount>0) dailyWeatherAdapter.removeItem(0)
            for (i in 1..7) {
                model.get(i)?.let { item ->
                    dailyWeatherAdapter.addItem(
                        DailyTable(
                            SimpleDateFormat("MM/dd").format(item.dt!! * 1000L),
                            "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png",
                            "${(item.temp.max).toInt()}",
                            "${(item.temp.min).toInt()}"
                        )
                    )
                }
            }
        }
    }

    private fun setViewFromDatabase(
        current: CurrentEntity,
        hourly: List<HourlyEntity>,
        daily: List<DailyEntity>
    ) {
        current.also {
            setCurrentData(
                HourlyAndCurrent(it.dt, it.temp, it.feelstemp, it.humidity, it.clouds, it.visibility, arrayListOf(WeatherOfHourlyAndCurrent(it.weatherid, it.main, it.description, it.icon)))
            )
        }

        var hourlyList = ArrayList<HourlyAndCurrent>()
        hourly.map {
            hourlyList.add(
                HourlyAndCurrent(it.dt, it.temp, it.feelstemp, it.humidity, it.clouds, it.visibility, arrayListOf(WeatherOfHourlyAndCurrent(it.weatherid, it.main, it.description, it.icon)))
            )
        }
        setHourlyData(hourlyList)

        var dailyList = ArrayList<Daily>()
        daily.map {
            dailyList.add(
                Daily(it.dt, DailyTemp(it.daytemp, it.mintemp, it.maxtemp), it.humidity, arrayListOf(WeatherOfHourlyAndCurrent(it.weatherid, it.main, it.description, it.icon)))
            )
        }
        setDailyData(dailyList)
    }
}