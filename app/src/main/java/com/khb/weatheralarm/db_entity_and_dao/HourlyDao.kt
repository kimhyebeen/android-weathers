package com.khb.weatheralarm.db_entity_and_dao

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