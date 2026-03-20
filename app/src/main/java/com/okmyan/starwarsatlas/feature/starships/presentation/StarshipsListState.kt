package com.okmyan.starwarsatlas.feature.starships.presentation

import com.okmyan.starwarsatlas.core.model.DataError
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem

data class StarshipsListState(
    val isLoading: Boolean = true,
    val items: List<StarshipListItem> = emptyList(),
    val error: DataError? = null,
)
