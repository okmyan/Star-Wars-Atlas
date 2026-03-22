package com.okmyan.starwarsatlas.feature.starships.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipDetails

sealed interface StarshipDetailsState {
    data object Loading : StarshipDetailsState
    data class Error(val error: DataError) : StarshipDetailsState
    data class Success(val starship: StarshipDetails) : StarshipDetailsState
}
