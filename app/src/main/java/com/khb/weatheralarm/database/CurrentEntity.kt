package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current")
data class CurrentEntity (
    @PrimaryKey val currid: Int,
    @ColumnInfo var dt: Long,
    @ColumnInfo var temp: Double,
    @ColumnInfo var feelstemp: Double,
    @ColumnInfo var humidity: Int,
    @ColumnInfo var clouds: Int,
    @ColumnInfo var visibility: Int,
    @ColumnInfo var weatherid: Int,
    @ColumnInfo var weathermain: String,
    @ColumnInfo var description: String,
    @ColumnInfo var icon: String
)