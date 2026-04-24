package com.okmyan.starwarsatlas.feature.people.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: FavoritePersonEntity)

    @Query("DELETE FROM favorite_people WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_people WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_people WHERE id = :id)")
    fun observeFavoriteById(id: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_people ORDER BY rowid ASC")
    fun observeFavoritePeople(): Flow<List<FavoritePersonEntity>>

}
