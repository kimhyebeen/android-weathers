package com.khb.weatheralarm.table_model

data class DailyTable(
    var date: String,
    var imageUri: String,
    var maxTemp: String,
    var minTemp: String
)