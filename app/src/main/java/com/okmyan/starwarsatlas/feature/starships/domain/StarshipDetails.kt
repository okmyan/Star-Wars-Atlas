package com.okmyan.starwarsatlas.feature.starships.domain

data class StarshipDetails(
    val name: String?,
    val model: String?,
    val starshipClass: String?,
    val manufacturers: List<String>,
    val costInCredits: Double?,
    val length: Double?,
    val crew: String?,
    val passengers: String?,
    val maxAtmosphericSpeed: Int?,
    val hyperdriveRating: Double?,
    val mglt: Int?,
    val cargoCapacity: Double?,
    val consumables: String?,
    val pilots: List<String>,
    val films: List<String>,
)
