package com.okmyan.starwarsatlas.feature.starships.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starships")
data class StarshipEntity(
    @PrimaryKey val id: String,
    val name: String,
    val filmCount: Int,
)
