package com.okmyan.starwarsatlas.feature.starships.domain

import kotlinx.collections.immutable.ImmutableList

data class StarshipDetails(
    val name: String?,
    val model: String?,
    val starshipClass: String?,
    val manufacturers: ImmutableList<String>,
    val costInCredits: Double?,
    val length: Double?,
    val crew: String?,
    val passengers: String?,
    val maxAtmosphericSpeed: Int?,
    val hyperdriveRating: Double?,
    val mglt: Int?,
    val cargoCapacity: Double?,
    val consumables: String?,
    val pilots: ImmutableList<String>,
    val films: ImmutableList<String>,
)
