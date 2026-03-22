package com.okmyan.starwarsatlas.feature.planets.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetDetails

sealed interface PlanetDetailsState {
    data object Loading : PlanetDetailsState
    data class Error(val error: DataError) : PlanetDetailsState
    data class Success(val planet: PlanetDetails) : PlanetDetailsState
}
