package com.khb.weatheralarm

import com.khb.weatheralarm.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface WeatherAPI {
    @GET("data/2.5/onecall")
    fun getWeatherList(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherModel>
}