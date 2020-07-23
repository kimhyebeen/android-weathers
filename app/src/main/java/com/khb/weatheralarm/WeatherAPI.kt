package com.khb.weatheralarm

import com.khb.weatheralarm.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface WeatherAPI {
    @GET("data/2.5/onecall")
    fun getWeatherList(@QueryMap map: Map<String, String>): Call<WeatherModel>
}