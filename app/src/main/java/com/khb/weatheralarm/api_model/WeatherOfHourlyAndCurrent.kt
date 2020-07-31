package com.khb.weatheralarm.api_model

data class WeatherOfHourlyAndCurrent (
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)