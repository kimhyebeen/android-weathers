package com.khb.weatheralarm.databaseitem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khb.weatheralarm.api_model.Daily
import com.khb.weatheralarm.api_model.HourlyAndCurrent

@Entity
data class Weather (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "current") val currentWeather: HourlyAndCurrent?,
    @ColumnInfo(name = "hourly") val hourlyWeather: ArrayList<HourlyAndCurrent>?,
    @ColumnInfo(name = "daily") val dailyWeather: ArrayList<Daily>?
)