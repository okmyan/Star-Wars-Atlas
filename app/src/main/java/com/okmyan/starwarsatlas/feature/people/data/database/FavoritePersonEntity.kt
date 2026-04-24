package com.okmyan.starwarsatlas.feature.people.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores people that the user has marked as favorites, in a table that is fully independent of
 * the paging cache ([PersonEntity]).
 *
 * Favorites are NOT stored as an `isFavorite` flag on [PersonEntity] because the paging cache is
 * ephemeral: when the cache timeout expires, the `people` table is cleared and re-populated from
 * the network one page at a time. Any favorite flag on a person who has not yet been re-fetched
 * would be lost. Keeping favorites in their own table means they survive cache invalidation
 * entirely — they are only written or deleted by an explicit user action.
 */
@Entity(tableName = "favorite_people")
data class FavoritePersonEntity(
    @PrimaryKey val id: String,
    val name: String,
    val filmCount: Int,
)
