package com.okmyan.starwarsatlas.feature.people.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apollographql.apollo.ApolloClient
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.people.domain.PersonDetails
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import com.okmyan.starwarsatlas.graphql.PersonDetailsQuery
import com.okmyan.starwarsatlas.utils.PAGE_SIZE
import com.okmyan.starwarsatlas.utils.outcomeOf
import com.okmyan.starwarsatlas.utils.requireData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val peopleDao: PeopleDao,
    private val lastRefreshDao: PeopleLastRefreshDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPeople(): Flow<PagingData<PersonListItem>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = PeopleRemoteMediator(apolloClient, peopleDao, lastRefreshDao),
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
                films = filmConnection?.films.orEmpty().filterNotNull().mapNotNull { it.title },
            )
        } ?: error("Person not found")
    }

}
