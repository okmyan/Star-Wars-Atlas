package com.okmyan.starwarsatlas.feature.people.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people_remote_keys")
data class PeopleRemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextCursor: String?,
)
