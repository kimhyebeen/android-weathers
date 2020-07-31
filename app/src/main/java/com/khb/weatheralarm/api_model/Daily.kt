package com.khb.weatheralarm.api_model

data class Daily(
    var dt: Long,
    var temp: DailyTemp,
    var humidity: Int,
    var weather: ArrayList<WeatherOfHourlyAndCurrent>
)