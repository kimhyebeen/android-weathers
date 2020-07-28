package com.khb.weatheralarm.model

data class DailyTableItem(
    var date: String,
    var imageUri: String,
    var maxTemp: String,
    var minTemp: String
)