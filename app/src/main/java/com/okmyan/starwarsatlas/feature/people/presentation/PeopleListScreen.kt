package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.okmyan.starwarsatlas.R
import com.okmyan.starwarsatlas.core.ui.common.components.CatalogPagingScreen
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem

@Composable
fun PeopleListScreen(
    viewModel: PeopleListViewModel = hiltViewModel(),
    onPersonClick: (String) -> Unit,
) {
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsStateWithLifecycle()
    val pagingItems = viewModel.items.collectAsLazyPagingItems()
    val favoritePeople by viewModel.favoritePeople.collectAsStateWithLifecycle()
    val favoriteIds = remember(favoritePeople) { favoritePeople.mapTo(HashSet()) { it.id } }
    val listState = rememberLazyListState()

    Column {
        FavoritesFilter(
            selected = showFavoritesOnly,
            onToggle = viewModel::toggleFavoriteFilter,
        )

        if (showFavoritesOnly) {
            FavoritePeopleContent(
                people = favoritePeople,
                onFavoriteToggle = viewModel::toggleFavorite,
                onPersonClick = onPersonClick,
            )
        } else {
            PeoplePagingContent(
                pagingItems = pagingItems,
                favoriteIds = favoriteIds,
                listState = listState,
                onFavoriteToggle = viewModel::toggleFavorite,
                onPersonClick = onPersonClick,
            )
        }
    }
}

@Composable
private fun FavoritesFilter(
    selected: Boolean,
    onToggle: () -> Unit,
) {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        FilterChip(
            selected = selected,
            onClick = onToggle,
            label = { Text(stringResource(R.string.favorites_filter)) },
            leadingIcon = { Icon(Icons.Filled.Star, contentDescription = null) },
        )
    }
}

@Composable
private fun FavoritePeopleContent(
    people: List<PersonListItem>,
    onFavoriteToggle: (String) -> Unit,
    onPersonClick: (String) -> Unit,
) {
    if (people.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.favorites_empty),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(people, key = { it.id }) { person ->
                PersonItemCard(
                    name = person.name,
                    filmCount = person.filmCount,
                    isFavorite = true,
                    onFavoriteToggle = { onFavoriteToggle(person.id) },
                    onClick = { onPersonClick(person.id) },
                )
            }
        }
    }
}

@Composable
private fun PeoplePagingContent(
    pagingItems: LazyPagingItems<PersonListItem>,
    favoriteIds: Set<String>,
    listState: LazyListState,
    onFavoriteToggle: (String) -> Unit,
    onPersonClick: (String) -> Unit,
) {
    CatalogPagingScreen(
        pagingItems = pagingItems,
        key = { it.id },
        listState = listState,
    ) { person ->
        PersonItemCard(
            name = person.name,
            filmCount = person.filmCount,
            isFavorite = person.id in favoriteIds,
            onFavoriteToggle = { onFavoriteToggle(person.id) },
            onClick = { onPersonClick(person.id) },
        )
    }
}
