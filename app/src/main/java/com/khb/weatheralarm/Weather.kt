package com.khb.weatheralarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khb.weatheralarm.model.WeatherDailyItems
import com.khb.weatheralarm.model.WeatherItems

@Entity
data class Weather (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "current") val currentWeather: WeatherItems?,
    @ColumnInfo(name = "hourly") val hourlyWeather: ArrayList<WeatherItems>?,
    @ColumnInfo(name = "daily") val dailyWeather: ArrayList<WeatherDailyItems>?
)