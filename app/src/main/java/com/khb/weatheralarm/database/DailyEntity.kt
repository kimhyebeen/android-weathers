package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class DailyEntity (
    @PrimaryKey val dailyid: Int,
    @ColumnInfo var dt: Long,
    @ColumnInfo var daytemp: Double,
    @ColumnInfo var mintemp: Double,
    @ColumnInfo var maxtemp: Double,
    @ColumnInfo var humidity: Int,
    @ColumnInfo var weatherid: Int,
    @ColumnInfo var weathermain: String,
    @ColumnInfo var description: String,
    @ColumnInfo var icon: String
)