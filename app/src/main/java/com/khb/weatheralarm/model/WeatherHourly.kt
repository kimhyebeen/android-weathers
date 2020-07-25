package com.khb.weatheralarm.model

data class WeatherHourly(
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var humidity: Int,
    var clouds: Int,
    var visibility: Int,
    var weather: ArrayList<WeatherSubItem>
)