package com.okmyan.starwarsatlas.feature.planets.presentation

import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem

data class PlanetsListState(
    val isLoading: Boolean = false,
    val items: List<PlanetListItem> = emptyList(),
    val errorMessage: String? = null,
)
