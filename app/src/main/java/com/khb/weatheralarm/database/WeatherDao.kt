package com.khb.weatheralarm.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface WeatherDao {
    @Query("SELECT * FROM WeatherEntity")
    fun getAll(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: WeatherEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(weather: WeatherEntity): Boolean
}