package com.okmyan.starwarsatlas.feature.planets.domain

data class PlanetDetails(
    val name: String?,
    val diameter: Int?,
    val rotationPeriod: Int?,
    val orbitalPeriod: Int?,
    val gravity: String?,
    val population: Double?,
    val climates: List<String>,
    val terrains: List<String>,
    val surfaceWater: Double?,
    val residents: List<String>,
    val films: List<String>,
)
