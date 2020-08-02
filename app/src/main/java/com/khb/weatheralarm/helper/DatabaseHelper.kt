package com.khb.weatheralarm.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khb.weatheralarm.database.WeatherDao
import com.khb.weatheralarm.database.WeatherEntity

@Database(entities = arrayOf(WeatherEntity::class), version = 1, exportSchema = false)
abstract class DatabaseHelper: RoomDatabase() {
    companion object {
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper? {
            instance?.let { return instance } ?: synchronized(DatabaseHelper::class) {
                instance = Room.databaseBuilder(context.applicationContext, DatabaseHelper::class.java, "weather.db").build()
                return instance
            }
        }
    }
}