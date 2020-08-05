package com.khb.weatheralarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class HourlyEntity (
    @PrimaryKey(autoGenerate = true) val hourlyid: Int = 0,
    @ColumnInfo var hourdt: Long? = 0L,
    @ColumnInfo var hourtemp: Double? = 0.0,
    @ColumnInfo var hourfeelstemp: Double? = 0.0,
    @ColumnInfo var hourhumidity: Int? = 0,
    @ColumnInfo var hourclouds: Int? = 0,
    @ColumnInfo var hourvisibility: Int? = 0,
    @ColumnInfo var hourweatherid: Int? = 0,
    @ColumnInfo var hourweathermain: String? = "",
    @ColumnInfo var hourdescription: String? = "",
    @ColumnInfo var houricon: String? = ""
) {
//    constructor() : this (null, null, null, null, null ,null, null,null, null, null)
}