package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class DailyEntity (
    @PrimaryKey(autoGenerate = true) val dailyid: Int = 0,
    @ColumnInfo var dailydt: Long? = 0L,
    @ColumnInfo var daytemp: Double? = 0.0,
    @ColumnInfo var mintemp: Double? = 0.0,
    @ColumnInfo var maxtemp: Double? = 0.0,
    @ColumnInfo var dailyhumidity: Int? = 0,
    @ColumnInfo var dailyweatherid: Int? = 0,
    @ColumnInfo var dailyweathermain: String? = "",
    @ColumnInfo var dailydescription: String? = "",
    @ColumnInfo var dailyicon: String? = ""
)