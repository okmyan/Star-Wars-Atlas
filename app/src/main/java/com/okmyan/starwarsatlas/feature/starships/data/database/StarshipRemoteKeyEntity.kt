package com.okmyan.starwarsatlas.feature.starships.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starships_remote_keys")
data class StarshipRemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextCursor: String?,
)
