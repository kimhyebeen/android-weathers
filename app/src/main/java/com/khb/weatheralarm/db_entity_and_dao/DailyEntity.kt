package com.khb.weatheralarm.db_entity_and_dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class DailyEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var dt: Long = 0L,
    @ColumnInfo var daytemp: Double = 0.0,
    @ColumnInfo var mintemp: Double = 0.0,
    @ColumnInfo var maxtemp: Double = 0.0,
    @ColumnInfo var humidity: Int = 0,
    @ColumnInfo var weatherid: Int = 0,
    @ColumnInfo var main: String = "",
    @ColumnInfo var description: String = "",
    @ColumnInfo var icon: String = ""
)