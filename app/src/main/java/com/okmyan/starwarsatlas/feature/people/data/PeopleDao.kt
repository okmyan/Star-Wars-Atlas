package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<PersonEntity>)

    @Query("DELETE FROM people")
    suspend fun deleteAllPeople()

    @Query("SELECT COUNT(id) FROM people")
    suspend fun count(): Int

    @Query("SELECT * FROM people ORDER BY rowid ASC")
    fun pagingSource(): PagingSource<Int, PersonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<PeopleRemoteKeyEntity>)

    @Query("SELECT * FROM people_remote_keys WHERE id = :id")
    suspend fun remoteKeyById(id: String): PeopleRemoteKeyEntity?

    @Query("DELETE FROM people_remote_keys")
    suspend fun deleteAllRemoteKeys()

    @Transaction
    suspend fun upsert(
        people: List<PersonEntity>,
        keys: List<PeopleRemoteKeyEntity>,
        clearFirst: Boolean,
    ) {
        if (clearFirst) {
            deleteAllPeople()
            deleteAllRemoteKeys()
        }
        insertPeople(people)
        insertRemoteKeys(keys)
    }

}
