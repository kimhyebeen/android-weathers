package com.khb.weatheralarm.helper

import android.content.Context
import com.khb.weatheralarm.R
import com.khb.weatheralarm.StringKeySet
import com.khb.weatheralarm.model.WeatherAPI
import com.khb.weatheralarm.model.WeatherApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper(context: Context) {
    val context: Context
    var retrofit: Retrofit
    var weatherAPI: WeatherAPI


    init {
        this.context = context
        this.retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    fun requestHourlyWeatherAPI(
        lat: String,
        lon: String,
        hourlyData: (WeatherApiModel) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_hourly), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<WeatherApiModel> {
            override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                println("hourly 실패 : $t")
            }

            override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                println("hourly 성공 : ${response.body().toString()}")
                response.body()?.let { hourlyData(it) }
            }
        })
    }

    fun requestCurrentWeatherAPI(
        lat: String,
        lon: String,
        currentData: (WeatherApiModel) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_current), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<WeatherApiModel> {
                override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                    println("current 실패 : $t")
                }

                override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                    println("current 성공 : ${response.body().toString()}")
                    response.body()?.let { currentData(it) }
                }
            })
    }

    fun requestDailyWeatherAPI(
        lat: String,
        lon: String,
        dailyData: (WeatherApiModel) -> Unit
    ) {
        weatherAPI.getWeatherList(lat, lon, context.getString(R.string.exclude_daily), StringKeySet.API_KEY, context.getString(R.string.units))
            ?.enqueue(object : Callback<WeatherApiModel> {
                override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                    println("daily 실패 : $t")
                }

                override fun onResponse(call: Call<WeatherApiModel>, response: Response<WeatherApiModel>) {
                    println("daily 성공 : ${response.body().toString()}")
                    response.body()?.let { dailyData(it) }
                }
            })
    }
}