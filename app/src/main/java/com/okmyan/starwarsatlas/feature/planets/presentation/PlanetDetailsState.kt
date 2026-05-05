package com.okmyan.starwarsatlas.feature.planets.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetDetails

data class PlanetDetailsState(
    val isLoading: Boolean = true,
    val error: DataError? = null,

    val planet: PlanetDetails? = null,
)
