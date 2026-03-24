package com.okmyan.starwarsatlas.feature.starships.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StarshipsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStarships(starships: List<StarshipEntity>)

    @Query("DELETE FROM starships")
    suspend fun deleteAllStarships()

    @Query("SELECT COUNT(id) FROM starships")
    suspend fun count(): Int

    @Query("SELECT * FROM starships ORDER BY rowid ASC")
    fun pagingSource(): PagingSource<Int, StarshipEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<StarshipRemoteKeyEntity>)

    @Query("SELECT * FROM starships_remote_keys WHERE id = :id")
    suspend fun remoteKeyById(id: String): StarshipRemoteKeyEntity?

    @Query("DELETE FROM starships_remote_keys")
    suspend fun deleteAllRemoteKeys()

    @Transaction
    suspend fun upsert(
        starships: List<StarshipEntity>,
        keys: List<StarshipRemoteKeyEntity>,
        clearFirst: Boolean,
    ) {
        if (clearFirst) {
            deleteAllStarships()
            deleteAllRemoteKeys()
        }
        insertStarships(starships)
        insertRemoteKeys(keys)
    }

}
