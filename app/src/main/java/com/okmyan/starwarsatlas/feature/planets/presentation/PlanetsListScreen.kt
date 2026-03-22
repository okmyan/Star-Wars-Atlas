package com.okmyan.starwarsatlas.feature.planets.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogItemCard
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogPagingScreen

@Composable
fun PlanetsListScreen(
    viewModel: PlanetsListViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.items.collectAsLazyPagingItems()

    CatalogPagingScreen(
        pagingItems = pagingItems,
        key = { it.id },
    ) { planet ->
        CatalogItemCard(
            name = planet.name,
            filmCount = planet.filmCount,
        )
    }
}
