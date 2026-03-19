package com.okmyan.starwarsatlas.feature.starships.presentation

import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem

data class StarshipsListState(
    val isLoading: Boolean = false,
    val items: List<StarshipListItem> = emptyList(),
    val errorMessage: String? = null,
)
