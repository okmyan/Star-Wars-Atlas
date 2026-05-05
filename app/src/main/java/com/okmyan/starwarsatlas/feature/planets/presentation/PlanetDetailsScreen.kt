package com.okmyan.starwarsatlas.feature.planets.presentation

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.starwarsatlas.R
import com.okmyan.starwarsatlas.core.ui.common.components.DetailRow
import com.okmyan.starwarsatlas.core.ui.common.components.DetailsTopBar
import com.okmyan.starwarsatlas.core.ui.common.components.ErrorContent
import com.okmyan.starwarsatlas.core.ui.common.components.Loading
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetDetails

@Composable
fun PlanetDetailsScreen(
    viewModel: PlanetDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    uiState.run {
        Scaffold(
            topBar = {
                DetailsTopBar(
                    name = planet?.name,
                    onBack = onBack,
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                when {
                    isLoading -> Loading()
                    error != null -> ErrorContent(
                        error = error,
                        onRetry = viewModel::retry,
                    )
                    planet != null -> PlanetDetailsContent(planet)
                }
            }
        }
    }
}

@Composable
private fun PlanetDetailsContent(planet: PlanetDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_diameter),
                value = planet.diameter?.let {
                    stringResource(R.string.planet_details_diameter_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_rotation_period),
                value = planet.rotationPeriod?.let {
                    stringResource(R.string.planet_details_rotation_period_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_orbital_period),
                value = planet.orbitalPeriod?.let {
                    stringResource(R.string.planet_details_orbital_period_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_gravity),
                value = planet.gravity,
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_population),
                value = planet.population?.let {
                    stringResource(R.string.planet_details_population_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_climates),
                value = planet.climates.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_terrains),
                value = planet.terrains.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_surface_water),
                value = planet.surfaceWater?.let {
                    stringResource(R.string.planet_details_surface_water_value, it)
                },
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.planet_details_residents),
                value = planet.residents.takeIf { it.isNotEmpty() }?.joinToString("\n"),
            )
        }
        item {
            DetailRow(
                label = stringResource(R.string.details_films),
                value = planet.films.takeIf { it.isNotEmpty() }?.joinToString("\n"),
                showDivider = false,
            )
        }
    }
}
