package com.khb.weatheralarm.api_model

data class MainApi(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var current: HourlyAndCurrent?,
    var daily: ArrayList<Daily>?,
    var hourly: ArrayList<HourlyAndCurrent>?
)