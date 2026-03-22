package com.okmyan.starwarsatlas.feature.people.domain

data class PersonDetails(
    val name: String?,
    val birthYear: String?,
    val gender: String?,
    val height: Int?,
    val mass: Double?,
    val eyeColor: String?,
    val hairColor: String?,
    val skinColor: String?,
    val homeworld: String?,
    val films: List<String>,
)
