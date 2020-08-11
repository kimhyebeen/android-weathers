package com.khb.weatheralarm.database_model

import androidx.room.*

@Dao
interface CurrentDao {
    @Transaction
    @Query("SELECT * FROM current")
    fun getCurrent(): List<CurrentEntity>

    @Insert
    fun insertCurrent(vararg current: CurrentEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrent(current: CurrentEntity)

    @Delete
    fun deleteCurrent(current: CurrentEntity)
}