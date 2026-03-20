package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem

data class PeopleListState(
    val isLoading: Boolean = false,
    val items: List<PersonListItem> = emptyList(),
    val error: DataError? = null,
)
