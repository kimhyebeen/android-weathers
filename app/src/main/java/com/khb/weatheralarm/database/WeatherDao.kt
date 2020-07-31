package com.khb.weatheralarm.database

import androidx.room.Insert
import androidx.room.Query

interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAll(): WeatherEntity

    @Insert
    fun updateAll(weather: WeatherEntity): Boolean
}