package com.okmyan.starwarsatlas.feature.planets.domain

import kotlinx.collections.immutable.ImmutableList

data class PlanetDetails(
    val name: String?,
    val diameter: Int?,
    val rotationPeriod: Int?,
    val orbitalPeriod: Int?,
    val gravity: String?,
    val population: Double?,
    val climates: ImmutableList<String>,
    val terrains: ImmutableList<String>,
    val surfaceWater: Double?,
    val residents: ImmutableList<String>,
    val films: ImmutableList<String>,
)
