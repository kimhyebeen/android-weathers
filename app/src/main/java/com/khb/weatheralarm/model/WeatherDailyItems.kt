package com.khb.weatheralarm.model

data class WeatherDailyItems(
    var dt: Long,
    var temp: WeatherDailyTemp,
    var humidity: Int,
    var weather: ArrayList<WeatherSubItem>
)