package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo var timezone: String
)