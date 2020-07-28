package com.khb.weatheralarm.model

data class WeatherApiModel(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var current: WeatherItems?,
    var daily: ArrayList<WeatherDailyItems>?,
    var hourly: ArrayList<WeatherItems>?
)