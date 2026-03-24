package com.okmyan.starwarsatlas.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.okmyan.starwarsatlas.feature.people.data.database.FavoritePeopleDao
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleDao
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.people.data.database.PersonEntity
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetEntity
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.planets.data.database.PlanetsDao
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipEntity
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipRemoteKeyEntity
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipsDao

@Database(
    entities = [
        PersonEntity::class,
        PeopleRemoteKeyEntity::class,
        StarshipEntity::class,
        StarshipRemoteKeyEntity::class,
        PlanetEntity::class,
        PlanetRemoteKeyEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class StarWarsAtlasDatabase : RoomDatabase() {

    abstract fun peopleDao(): PeopleDao

    abstract fun favoritePeopleDao(): FavoritePeopleDao

    abstract fun starshipsDao(): StarshipsDao

    abstract fun planetsDao(): PlanetsDao

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
                .fallbackToDestructiveMigration(false)
                .build()
    }

}
