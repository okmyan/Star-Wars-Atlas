package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipDetails

@Composable
fun StarshipDetailsScreen(
    viewModel: StarshipDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DetailsTopBar(
                name = (uiState as? StarshipDetailsState.Success)?.starship?.name,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when (val state = uiState) {
                is StarshipDetailsState.Loading -> Loading()
                is StarshipDetailsState.Error -> ErrorContent(
                    error = state.error,
                    onRetry = viewModel::retry,
                )
                is StarshipDetailsState.Success -> StarshipDetailsContent(state.starship)
            }
        }
    }
}

@Composable
private fun StarshipDetailsContent(starship: StarshipDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_model),
                value = starship.model,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_class),
                value = starship.starshipClass,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_manufacturers),
                value = starship.manufacturers.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_cost),
                value = starship.costInCredits?.let {
                    stringResource(R.string.starship_details_cost_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_length),
                value = starship.length?.let {
                    stringResource(R.string.starship_details_length_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_crew),
                value = starship.crew,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_passengers),
                value = starship.passengers,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_max_speed),
                value = starship.maxAtmosphericSpeed?.let {
                    stringResource(R.string.starship_details_max_speed_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_hyperdrive_rating),
                value = starship.hyperdriveRating?.let {
                    stringResource(R.string.starship_details_hyperdrive_rating_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_mglt),
                value = starship.mglt?.let {
                    stringResource(R.string.starship_details_mglt_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_cargo_capacity),
                value = starship.cargoCapacity?.let {
                    stringResource(R.string.starship_details_cargo_capacity_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_consumables),
                value = starship.consumables,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.starship_details_pilots),
                value = starship.pilots.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.details_films),
                value = starship.films.takeIf { it.isNotEmpty() }?.joinToString("\n"),
                showDivider = false,
            )
        }
    }
}
