package com.okmyan.starwarsatlas.feature.people.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people_last_refresh")
data class PeopleLastRefreshEntity(
    @PrimaryKey val id: Int = 0,
    val timestamp: Long,
)