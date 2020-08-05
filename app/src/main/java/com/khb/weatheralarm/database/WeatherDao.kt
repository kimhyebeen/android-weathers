package com.khb.weatheralarm.database

import androidx.room.*

@Dao
interface WeatherDao {
    @Transaction
    @Query("SELECT * FROM weather")
    fun getWeather(): List<WeatherWithAllEntities>

    @Insert
    fun insert(vararg test: WeatherWithAllEntities)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(test: WeatherWithAllEntities)

    @Delete
    fun deleteAll(weather: WeatherWithAllEntities)
}