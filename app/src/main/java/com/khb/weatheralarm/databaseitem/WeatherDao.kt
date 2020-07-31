package com.khb.weatheralarm.databaseitem

import androidx.room.Insert
import androidx.room.Query
import com.khb.weatheralarm.databaseitem.Weather

interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAll(): Weather

    @Insert
    fun updateAll(weather: Weather): Boolean
}