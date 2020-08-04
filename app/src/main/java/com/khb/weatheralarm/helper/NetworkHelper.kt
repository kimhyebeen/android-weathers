package com.khb.weatheralarm.helper

import android.content.Context
import com.khb.weatheralarm.R
import com.khb.weatheralarm.StringKeySet
import com.khb.weatheralarm.WeatherAPI
import com.khb.weatheralarm.api_model.Daily
import com.khb.weatheralarm.api_model.HourlyAndCurrent
import com.khb.weatheralarm.api_model.MainApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper(context: Context) {
    val context: Context
//    val databaseHelper: DatabaseHelper
    var retrofit: Retrofit
    var weatherAPI: WeatherAPI

    init {
        this.context = context
        this.retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.weatherAPI = retrofit.create(WeatherAPI::class.java)
//        this.databaseHelper = DatabaseHelper.getInstance(context)
    }

    fun requestHourlyWeatherAPI(
        lat: String,
        lon: String,
        hourlyData: (ArrayList<HourlyAndCurrent>) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_hourly), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<MainApi> {
            override fun onFailure(call: Call<MainApi>, t: Throwable) {
                println("hourly 실패 : $t")
            }

            override fun onResponse(call: Call<MainApi>, response: Response<MainApi>) {
                println("hourly 성공 : ${response.body().toString()}")
//                var hourly = databaseHelper.weatherDao().getWeather()[0]
//                hourly.hourly = response.body()?.hourly!!
//                databaseHelper.weatherDao().updateAll(hourly)

                response.body()?.let { hourlyData(it.hourly!!) }
            }
        })
    }

    fun requestCurrentWeatherAPI(
        lat: String,
        lon: String,
        currentData: (HourlyAndCurrent) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_current), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<MainApi> {
                override fun onFailure(call: Call<MainApi>, t: Throwable) {
                    println("current 실패 : $t")
                }

                override fun onResponse(call: Call<MainApi>, response: Response<MainApi>) {
                    println("current 성공 : ${response.body().toString()}")
//                    var current = databaseHelper.weatherDao().getWeather()[0]
//                    current.current = response.body()?.current!!
//                    databaseHelper.weatherDao().updateAll(current)

                    response.body()?.let { currentData(it.current!!) }
                }
            })
    }

    fun requestDailyWeatherAPI(
        lat: String,
        lon: String,
        dailyData: (ArrayList<Daily>) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_daily), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<MainApi> {
                override fun onFailure(call: Call<MainApi>, t: Throwable) {
                    println("daily 실패 : $t")
                }

                override fun onResponse(call: Call<MainApi>, response: Response<MainApi>) {
                    println("daily 성공 : ${response.body().toString()}")
//                    var daily = databaseHelper.weatherDao().getWeather()[0]
//                    daily.daily = response.body()?.daily
//                    databaseHelper.weatherDao().updateAll(daily)

                    response.body()?.let { dailyData(it.daily!!) }
                }
            })
    }
}