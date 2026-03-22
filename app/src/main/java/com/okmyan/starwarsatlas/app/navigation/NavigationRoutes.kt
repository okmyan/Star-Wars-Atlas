package com.okmyan.starwarsatlas.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PeopleGraph

@Serializable
data object PeopleList

@Serializable
data object StarshipsGraph

@Serializable
data object StarshipsList

@Serializable
data object PlanetsGraph

@Serializable
data object PlanetsList

@Serializable
data class PersonDetails(val personId: String)
