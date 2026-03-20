package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogItemCard
import com.okmyan.starwarsatlas.core.ui.common.components.ErrorContent
import com.okmyan.starwarsatlas.core.ui.common.components.Loading
import com.okmyan.starwarsatlas.core.ui.theme.StarWarsAtlasTheme
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem

@Composable
fun StarshipsListScreen(
    viewModel: StarshipsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StarshipsListContent(uiState = uiState, onRetry = viewModel::loadStarships)
}

@Composable
private fun StarshipsListContent(
    uiState: StarshipsListState,
    onRetry: () -> Unit,
) {
    val error = uiState.error

    when {
        uiState.isLoading -> Loading()

        error != null -> ErrorContent(
            error = error,
            onRetry = onRetry,
        )

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = uiState.items,
                    key = { it.id },
                ) { starship ->
                    CatalogItemCard(
                        name = starship.name,
                        filmCount = starship.filmCount,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StarshipsListContentPreview() {
    StarWarsAtlasTheme {
        StarshipsListContent(
            uiState = StarshipsListState(
                items = listOf(
                    StarshipListItem(id = "1", name = "Millennium Falcon", filmCount = 8),
                    StarshipListItem(id = "2", name = "X-wing", filmCount = 8),
                    StarshipListItem(id = "3", name = "Star Destroyer", filmCount = 8),
                ),
            ),
            onRetry = {},
        )
    }
}
