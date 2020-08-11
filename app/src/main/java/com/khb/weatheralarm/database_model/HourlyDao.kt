package com.khb.weatheralarm.database_model

import androidx.room.*

@Dao
interface HourlyDao {
    @Transaction
    @Query("SELECT * FROM hourly")
    fun getHourly(): List<HourlyEntity>

    @Insert
    fun insertHourly(vararg hourly: HourlyEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHourly(hourly: HourlyEntity)

    @Delete
    fun deleteHourly(hourly: HourlyEntity)
}