package com.okmyan.starwarsatlas.feature.people.presentation

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
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem

@Composable
fun PeopleListScreen(
    viewModel: PeopleListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PeopleListContent(uiState = uiState, onRetry = viewModel::loadPeople)
}

@Composable
private fun PeopleListContent(
    uiState: PeopleListState,
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
                ) { person ->
                    CatalogItemCard(
                        name = person.name,
                        filmCount = person.filmCount,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PeopleListContentPreview() {
    StarWarsAtlasTheme {
        PeopleListContent(
            uiState = PeopleListState(
                items = listOf(
                    PersonListItem(id = "1", name = "Luke Skywalker", filmCount = 7),
                    PersonListItem(id = "2", name = "Darth Vader", filmCount = 7),
                    PersonListItem(id = "3", name = "Leia Organa", filmCount = 8),
                ),
            ),
            onRetry = {},
        )
    }
}
