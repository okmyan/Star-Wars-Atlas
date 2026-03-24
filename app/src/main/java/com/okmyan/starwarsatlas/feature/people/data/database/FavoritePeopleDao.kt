package com.okmyan.starwarsatlas.feature.people.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePeopleDao {

    @Query("UPDATE people SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM people WHERE id = :id")
    suspend fun isFavorite(id: String): Boolean?

    @Query("SELECT isFavorite FROM people WHERE id = :id")
    fun observeFavoriteById(id: String): Flow<Boolean?>

    @Query("SELECT * FROM people WHERE isFavorite = 1 ORDER BY rowid ASC")
    fun observeFavoritePeople(): Flow<List<PersonEntity>>

}
