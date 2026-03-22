package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogItemCard
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogPagingScreen

@Composable
fun PeopleListScreen(
    viewModel: PeopleListViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.items.collectAsLazyPagingItems()

    CatalogPagingScreen(
        pagingItems = pagingItems,
        key = { it.id },
    ) { person ->
        CatalogItemCard(
            name = person.name,
            filmCount = person.filmCount
        )
    }
}
