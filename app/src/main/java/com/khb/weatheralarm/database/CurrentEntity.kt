package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CurrentEntity (
    @PrimaryKey(autoGenerate = true) val currid: Int = 0,
    @ColumnInfo var curdt: Long? = 0L,
    @ColumnInfo var curtemp: Double? = 0.0,
    @ColumnInfo var curfeelstemp: Double? = 0.0,
    @ColumnInfo var curhumidity: Int? = 0,
    @ColumnInfo var curclouds: Int? = 0,
    @ColumnInfo var curvisibility: Int? = 0,
    @ColumnInfo var curweatherid: Int? = 0,
    @ColumnInfo var curweathermain: String? = "",
    @ColumnInfo var curdescription: String? = "",
    @ColumnInfo var curicon: String? = ""
)