package com.khb.weatheralarm.model

data class WeatherHourly(
    val dt: Long?,
    val temp: Double?,
    val feels_like: Double?,
    val humidity: Int?,
    val clouds: Int?,
    val visibility: Int?,
    val weather: ArrayList<WeatherSubItem>?
)