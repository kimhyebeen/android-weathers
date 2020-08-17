package com.khb.weatheralarm.helper

import android.content.Context
import com.khb.weatheralarm.R
import com.khb.weatheralarm.StringKeySet
import com.khb.weatheralarm.WeatherAPI
import com.khb.weatheralarm.api_model.Daily
import com.khb.weatheralarm.api_model.HourlyAndCurrent
import com.khb.weatheralarm.api_model.MainApi
import com.khb.weatheralarm.database_model.CurrentEntity
import com.khb.weatheralarm.database_model.DailyEntity
import com.khb.weatheralarm.database_model.HourlyEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper(context: Context) {
    val context: Context
    val databaseHelper: DatabaseHelper
    var retrofit: Retrofit
    var weatherAPI: WeatherAPI

    init {
        this.context = context
        this.retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.weatherAPI = retrofit.create(WeatherAPI::class.java)
        this.databaseHelper = DatabaseHelper.getInstance(context)
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

                response.body()?.let {
                    hourlyData(it.hourly!!)
                    for (i in 0..23) {
                        it.hourly!![i].let{
                            HourlyEntity(i+101,
                                it.dt,
                                it.temp,
                                it.feels_like,
                                it.humidity,
                                it.clouds,
                                it.visibility,
                                it.weather[0].id,
                                it.weather[0].main,
                                it.weather[0].description,
                                it.weather[0].icon
                            ).let { entity ->
                                GlobalScope.launch {
                                    if (databaseHelper.hourlyDao().getHourly().size >= 24)
                                        databaseHelper.hourlyDao().updateHourly(entity)
                                    else
                                        databaseHelper.hourlyDao().insertHourly(entity)
                                }
                            }
                        }
                    }
                }
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

                    response.body()?.let {
                        currentData(it.current!!)
                        CurrentEntity(101,
                            it.timezone,
                            it.current!!.dt,
                            it.current!!.temp,
                            it.current!!.feels_like,
                            it.current!!.humidity,
                            it.current!!.clouds,
                            it.current!!.visibility,
                            it.current!!.weather[0].id,
                            it.current!!.weather[0].main,
                            it.current!!.weather[0].description,
                            it.current!!.weather[0].icon
                        ).let {
                            GlobalScope.launch {
                                if (databaseHelper.currentDao().getCurrent().size > 0)
                                    databaseHelper.currentDao().updateCurrent(it)
                                else
                                    databaseHelper.currentDao().insertCurrent(it)
                            }
                        }
                    }
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

                    response.body()?.let {
                        dailyData(it.daily!!)
                        for (i in 0..7) {
                            it.daily!![i].let{
                                DailyEntity(i+101,
                                    it.dt,
                                    it.temp.day,
                                    it.temp.min,
                                    it.temp.max,
                                    it.humidity,
                                    it.weather[0].id,
                                    it.weather[0].main,
                                    it.weather[0].description,
                                    it.weather[0].icon
                                ).let { entity ->
                                    GlobalScope.launch {
                                        if (databaseHelper.dailyDao().getDaily().size >= 8)
                                            databaseHelper.dailyDao().updateDaily(entity)
                                        else
                                            databaseHelper.dailyDao().insertDaily(entity)
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}