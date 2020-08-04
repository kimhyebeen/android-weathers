//package com.khb.weatheralarm.database
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
import androidx.room.PrimaryKey
//import com.khb.weatheralarm.api_model.Daily
//import com.khb.weatheralarm.api_model.HourlyAndCurrent
//import com.khb.weatheralarm.api_model.WeatherOfHourlyAndCurrent
//
//@Entity(tableName = "weather")
data class WeatherEntity (
    @PrimaryKey val uid: Int
//    @ColumnInfo var timezone: String?,
//    @ColumnInfo var current: HourlyAndCurrent?,
//    @ColumnInfo var hourly: ArrayList<HourlyAndCurrent>?,
//    @ColumnInfo var daily: ArrayList<Daily?>?
)