package com.khb.weatheralarm.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity (
    @NonNull
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var timezone: String? = "",
    @Embedded var current: CurrentEntity? = null,
    @Embedded var hourly: List<HourlyEntity>? = null,
    @Embedded var daily: List<DailyEntity>? = null
)