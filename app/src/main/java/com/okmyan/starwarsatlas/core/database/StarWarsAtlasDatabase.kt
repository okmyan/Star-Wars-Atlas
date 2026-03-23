package com.okmyan.starwarsatlas.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.okmyan.starwarsatlas.feature.people.data.PeopleDao
import com.okmyan.starwarsatlas.feature.people.data.PeopleLastRefreshDao
import com.okmyan.starwarsatlas.feature.people.data.PeopleLastRefreshEntity
import com.okmyan.starwarsatlas.feature.people.data.PeopleRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.people.data.PersonEntity

@Database(
    entities = [
        PersonEntity::class,
        PeopleRemoteKeyEntity::class,
        PeopleLastRefreshEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class StarWarsAtlasDatabase : RoomDatabase() {

    abstract fun peopleDao(): PeopleDao
    abstract fun peopleLastRefreshDao(): PeopleLastRefreshDao

    companion object {

        private const val DATABASE_NAME = "star_wars_atlas.db"

        @Volatile
        private var INSTANCE: StarWarsAtlasDatabase? = null

        fun getInstance(context: Context): StarWarsAtlasDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                StarWarsAtlasDatabase::class.java,
                DATABASE_NAME
            )
                .build()
    }

}
