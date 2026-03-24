package com.okmyan.starwarsatlas.feature.planets.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets_remote_keys")
data class PlanetRemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextCursor: String?,
)
