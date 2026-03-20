package com.okmyan.starwarsatlas.feature.planets.presentation

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
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem

@Composable
fun PlanetsListScreen(
    viewModel: PlanetsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    PlanetsListContent(uiState = uiState, onRetry = viewModel::loadPlanets)
}

@Composable
private fun PlanetsListContent(
    uiState: PlanetsListState,
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
                ) { planet ->
                    CatalogItemCard(
                        name = planet.name,
                        filmCount = planet.filmCount,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanetsListContentPreview() {
    StarWarsAtlasTheme {
        PlanetsListContent(
            uiState = PlanetsListState(
                isLoading = false,
                items = listOf(
                    PlanetListItem(id = "1", name = "Tatooine", filmCount = 7),
                    PlanetListItem(id = "2", name = "Alderaan", filmCount = 2),
                    PlanetListItem(id = "3", name = "Hoth", filmCount = 1),
                ),
            ),
            onRetry = {},
        )
    }
}
