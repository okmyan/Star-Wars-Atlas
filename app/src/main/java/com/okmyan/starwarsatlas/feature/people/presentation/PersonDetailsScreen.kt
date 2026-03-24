package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.starwarsatlas.R
import com.okmyan.starwarsatlas.core.ui.common.components.DetailRow
import com.okmyan.starwarsatlas.core.ui.common.components.DetailsTopBar
import com.okmyan.starwarsatlas.core.ui.common.components.ErrorContent
import com.okmyan.starwarsatlas.core.ui.common.components.Loading
import com.okmyan.starwarsatlas.feature.people.domain.PersonDetails

@Composable
fun PersonDetailsScreen(
    viewModel: PersonDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DetailsTopBar(
                name = (uiState as? PersonDetailsState.Success)?.person?.name,
                onBack = onBack,
                actions = {
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = { viewModel.toggleFavorite() },
                        enabled = uiState is PersonDetailsState.Success,
                    ) {
                        if (isFavorite) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = stringResource(R.string.remove_from_favorites),
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.StarOutline,
                                contentDescription = stringResource(R.string.add_to_favorites),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when (val state = uiState) {
                is PersonDetailsState.Loading -> Loading()
                is PersonDetailsState.Error -> ErrorContent(
                    error = state.error,
                    onRetry = viewModel::retry,
                )

                is PersonDetailsState.Success -> PersonDetailsContent(state.person)
            }
        }
    }
}

@Composable
private fun PersonDetailsContent(person: PersonDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            DetailRow(
                label = stringResource(R.string.person_details_birth_year),
                value = person.birthYear,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_gender),
                value = person.gender,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_height),
                value = person.height?.let {
                    stringResource(R.string.person_details_height_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_mass),
                value = person.mass?.let {
                    stringResource(R.string.person_details_mass_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_eye_color),
                value = person.eyeColor,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_hair_color),
                value = person.hairColor,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_skin_color),
                value = person.skinColor,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.person_details_homeworld),
                value = person.homeworld,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.details_films),
                value = person.films.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
    }
}
