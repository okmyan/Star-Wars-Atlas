package com.okmyan.starwarsatlas.feature.planets.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val filmCount: Int,
)
