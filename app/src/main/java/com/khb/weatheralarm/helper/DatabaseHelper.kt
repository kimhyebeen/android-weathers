package com.khb.weatheralarm.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khb.weatheralarm.database_model.*

@Database(entities = arrayOf(CurrentEntity::class, HourlyEntity::class, DailyEntity::class), version = 1)
abstract class DatabaseHelper: RoomDatabase() {
    abstract fun currentDao(): CurrentDao
    abstract fun hourlyDao(): HourlyDao
    abstract fun dailyDao(): DailyDao

    companion object {
        // Volatile : 스레드가 직접 mail memory에 접근해서 읽을 수 있도록 하는 어노테이션 (?)
        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            instance?.let { return instance!! } ?: synchronized(DatabaseHelper::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "eachweather.db"
                ).build()
                return instance!!
            }
        }
    }
}