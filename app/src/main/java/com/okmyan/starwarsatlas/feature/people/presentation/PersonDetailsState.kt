package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.people.domain.PersonDetails

sealed interface PersonDetailsState {
    data object Loading : PersonDetailsState
    data class Error(val error: DataError) : PersonDetailsState
    data class Success(val person: PersonDetails) : PersonDetailsState
}
