package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem

data class PeopleListState(
    val isLoading: Boolean = false,
    val items: List<PersonListItem> = emptyList(),
    val errorMessage: String? = null,
)
