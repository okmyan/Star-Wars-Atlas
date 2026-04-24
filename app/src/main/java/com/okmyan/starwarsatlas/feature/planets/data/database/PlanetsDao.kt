package com.okmyan.starwarsatlas.feature.planets.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlanetsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanets(planets: List<PlanetEntity>)

    @Query("DELETE FROM planets")
    suspend fun deleteAllPlanets()

    @Query("SELECT COUNT(id) FROM planets")
    suspend fun count(): Int

    @Query("SELECT * FROM planets ORDER BY rowid ASC")
    fun pagingSource(): PagingSource<Int, PlanetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<PlanetRemoteKeyEntity>)

    @Query("SELECT * FROM planets_remote_keys WHERE id = :id")
    suspend fun remoteKeyById(id: String): PlanetRemoteKeyEntity?

    @Query("DELETE FROM planets_remote_keys")
    suspend fun deleteAllRemoteKeys()

    @Transaction
    suspend fun save(
        planets: List<PlanetEntity>,
        keys: List<PlanetRemoteKeyEntity>,
        clearFirst: Boolean,
    ) {
        if (clearFirst) {
            deleteAllPlanets()
            deleteAllRemoteKeys()
        }
        insertPlanets(planets)
        insertRemoteKeys(keys)
    }

}
