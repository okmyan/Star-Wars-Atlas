package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem

data class FavoritePeopleListState(
    val showFavoritesOnly: Boolean = false,
    val favoritePeople: List<PersonListItem> = emptyList(),
)
