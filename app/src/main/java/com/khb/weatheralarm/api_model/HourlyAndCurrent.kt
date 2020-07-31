package com.khb.weatheralarm.api_model

data class HourlyAndCurrent(
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var humidity: Int,
    var clouds: Int,
    var visibility: Int,
    var weather: ArrayList<WeatherOfHourlyAndCurrent>
)