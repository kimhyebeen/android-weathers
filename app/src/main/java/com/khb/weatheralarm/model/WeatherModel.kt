package com.khb.weatheralarm.model

data class WeatherModel(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var current: WeatherItems,
    var hourly: ArrayList<WeatherItems>?
)