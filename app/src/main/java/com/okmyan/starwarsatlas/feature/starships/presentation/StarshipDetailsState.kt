package com.okmyan.starwarsatlas.feature.starships.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipDetails

data class StarshipDetailsState(
    val isLoading: Boolean = true,
    val error: DataError? = null,

    val starship: StarshipDetails? = null,
)
