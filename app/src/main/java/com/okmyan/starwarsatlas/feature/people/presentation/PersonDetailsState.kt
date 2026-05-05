package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.people.domain.PersonDetails

data class PersonDetailsState(
    val isLoading: Boolean = true,
    val error: DataError? = null,

    val person: PersonDetails? = null,
    val isFavorite: Boolean = false,
)
