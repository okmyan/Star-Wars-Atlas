package com.okmyan.starwarsatlas.di

import android.content.Context
import com.okmyan.starwarsatlas.core.database.StarWarsAtlasDatabase
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleDao
import com.okmyan.starwarsatlas.feature.starships.data.database.StarshipsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): StarWarsAtlasDatabase =
        StarWarsAtlasDatabase.getInstance(context)

    @Provides
    fun providePeopleDao(db: StarWarsAtlasDatabase): PeopleDao = db.peopleDao()

    @Provides
    fun provideStarshipsDao(db: StarWarsAtlasDatabase): StarshipsDao = db.starshipsDao()

}
