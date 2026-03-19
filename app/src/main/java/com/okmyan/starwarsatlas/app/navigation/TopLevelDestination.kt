package com.okmyan.starwarsatlas.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    data object People : TopLevelDestination(
        route = "people",
        label = "People",
        icon = Icons.Outlined.Groups,
    )

    data object Starships : TopLevelDestination(
        route = "starships",
        label = "Starships",
        icon = Icons.Outlined.Rocket,
    )

    data object Planets : TopLevelDestination(
        route = "planets",
        label = "Planets",
        icon = Icons.Outlined.Language,
    )
}
