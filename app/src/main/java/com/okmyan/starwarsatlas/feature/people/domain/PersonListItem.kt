package com.okmyan.starwarsatlas.feature.people.domain

data class PersonListItem(
    val id: String,
    val name: String,
    val filmCount: Int,
    val isFavorite: Boolean = false,
)
