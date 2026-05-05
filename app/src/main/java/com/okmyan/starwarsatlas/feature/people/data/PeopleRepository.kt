package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.people.data.database.FavoritePeopleDao
import com.okmyan.starwarsatlas.feature.people.data.database.FavoritePersonEntity
import com.okmyan.starwarsatlas.feature.people.data.database.PeopleDao
import com.okmyan.starwarsatlas.feature.people.domain.PersonDetails
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.graphql.PersonDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val peopleDao: PeopleDao,
    private val favoritePeopleDao: FavoritePeopleDao,
    private val lastRefreshDataStore: LastRefreshDataStore,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPeople(): Flow<PagingData<PersonListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = PeopleRemoteMediator(apolloClient, peopleDao, lastRefreshDataStore),
        pagingSourceFactory = { peopleDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { entity ->
            PersonListItem(
                id = entity.id,
                name = entity.name,
                filmCount = entity.filmCount,
            )
        }
    }

    fun observeFavoritePeople(): Flow<List<PersonListItem>> =
        favoritePeopleDao.observeFavoritePeople().map { entities ->
            entities.map { entity ->
                PersonListItem(
                    id = entity.id,
                    name = entity.name,
                    filmCount = entity.filmCount,
                    isFavorite = true,
                )
            }
        }

    fun observeFavoriteById(id: String): Flow<Boolean> = favoritePeopleDao.observeFavoriteById(id)

    suspend fun toggleFavorite(id: String) {
        if (favoritePeopleDao.isFavorite(id)) {
            favoritePeopleDao.delete(id)
        } else {
            val person = peopleDao.getById(id) ?: run {
                Timber.w("toggleFavorite: person with id=$id not found in database")
                return
            }

            favoritePeopleDao.insert(
                FavoritePersonEntity(
                    id = person.id,
                    name = person.name,
                    filmCount = person.filmCount,
                )
            )
        }
    }

    suspend fun getPersonDetails(id: String): Outcome<PersonDetails> = outcomeOf {
        val data = apolloClient.query(
            PersonDetailsQuery(id = id)
        ).execute().requireData()

        data.person?.run {
            PersonDetails(
                name = name,
                birthYear = birthYear,
                gender = gender,
                height = height,
                mass = mass,
                eyeColor = eyeColor,
                hairColor = hairColor,
                skinColor = skinColor,
                homeworld = homeworld?.name,
                films = filmConnection?.films.orEmpty().filterNotNull().mapNotNull { it.title }
                    .toImmutableList(),
            )
        } ?: error("Person not found")
    }

}
