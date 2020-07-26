package com.khb.weatheralarm.model

data class DailyItems(
    var dt: Long,
    var temp: DailyTemp,
    var humidity: Int,
    var weather: ArrayList<WeatherSubItem>
)