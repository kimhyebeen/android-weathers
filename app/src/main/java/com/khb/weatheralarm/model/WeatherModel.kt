package com.khb.weatheralarm.model

data class WeatherModel(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Int?,
    val hourly: ArrayList<WeatherHourly>?
)