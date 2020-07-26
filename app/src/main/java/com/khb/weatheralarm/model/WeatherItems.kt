package com.khb.weatheralarm.model

data class WeatherItems(
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var humidity: Int,
    var clouds: Int,
    var visibility: Int,
    var weather: ArrayList<WeatherSubItem>
)