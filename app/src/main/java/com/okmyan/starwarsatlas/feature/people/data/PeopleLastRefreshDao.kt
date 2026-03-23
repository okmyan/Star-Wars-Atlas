package com.okmyan.starwarsatlas.feature.people.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PeopleLastRefreshDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLastRefresh(entity: PeopleLastRefreshEntity)

    // TODO We could use DataStore instead
    @Query("SELECT * FROM people_last_refresh WHERE id = 0")
    suspend fun lastRefresh(): PeopleLastRefreshEntity?

}
