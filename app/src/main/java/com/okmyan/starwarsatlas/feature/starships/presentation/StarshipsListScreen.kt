package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogItemCard
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogPagingScreen

@Composable
fun StarshipsListScreen(
    viewModel: StarshipsListViewModel = hiltViewModel(),
    onStarshipClick: (String) -> Unit = {},
) {
    val pagingItems = viewModel.items.collectAsLazyPagingItems()

    CatalogPagingScreen(
        pagingItems = pagingItems,
        key = { it.id },
    ) { starship ->
        CatalogItemCard(
            name = starship.name,
            filmCount = starship.filmCount,
            onClick = { onStarshipClick(starship.id) },
        )
    }
}
