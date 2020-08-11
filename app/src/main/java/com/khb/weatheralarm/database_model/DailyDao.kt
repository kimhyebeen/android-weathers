package com.khb.weatheralarm.database_model

import androidx.room.*

@Dao
interface DailyDao {
    @Transaction
    @Query("SELECT * FROM daily")
    fun getDaily(): List<DailyEntity>

    @Insert
    fun insertDaily(vararg daily: DailyEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDaily(daily: DailyEntity)

    @Delete
    fun deleteDaily(daily: DailyEntity)
}