package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoritePeopleListState(
    val showFavoritesOnly: Boolean = false,
    val favoritePeople: ImmutableList<PersonListItem> = persistentListOf(),
)
