package com.khb.weatheralarm.database

import androidx.room.*

@Dao
interface WeatherDao {
    @Transaction
    @Query("SELECT * FROM weather")
    fun getWeather(): List<WeatherEntity>

    @Insert
    fun insert(vararg test: WeatherEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(test: WeatherEntity)

    @Delete
    fun deleteAll(weather: WeatherEntity)
}