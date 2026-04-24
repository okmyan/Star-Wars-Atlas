package com.okmyan.starwarsatlas.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.okmyan.starwarsatlas.feature.people.data.database.FavoritePeopleDao
import com.okmyan.starwarsatlas.feature.people.data.database.FavoritePersonEntity
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
        FavoritePersonEntity::class,
        StarshipEntity::class,
        StarshipRemoteKeyEntity::class,
        PlanetEntity::class,
        PlanetRemoteKeyEntity::class,
    ],
    version = 2,
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
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration(false)
                .build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Create the new dedicated favorites table
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `favorite_people` (
                        `id` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `filmCount` INTEGER NOT NULL,
                        PRIMARY KEY (`id`)
                    )
                    """.trimIndent()
                )

                // 2. Migrate any existing favorites from the old people table
                db.execSQL(
                    """
                    INSERT INTO `favorite_people` (`id`, `name`, `filmCount`)
                    SELECT `id`, `name`, `filmCount` FROM `people` WHERE `isFavorite` = 1
                    """.trimIndent()
                )

                // 3. Recreate the people table without the isFavorite column
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `people_new` (
                        `id` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `filmCount` INTEGER NOT NULL,
                        PRIMARY KEY (`id`)
                    )
                    """.trimIndent()
                )
                db.execSQL("INSERT INTO `people_new` SELECT `id`, `name`, `filmCount` FROM `people`")
                db.execSQL("DROP TABLE `people`")
                db.execSQL("ALTER TABLE `people_new` RENAME TO `people`")
            }
        }

    }

}
