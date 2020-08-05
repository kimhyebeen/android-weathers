package com.khb.weatheralarm.database

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithAllEntities (
    @Embedded val weather: WeatherEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "currid"
    )
    var current: CurrentEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "hourlyid"
    )
    var hourly: List<HourlyEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "dailyid"
    )
    var daily: List<DailyEntity>
)