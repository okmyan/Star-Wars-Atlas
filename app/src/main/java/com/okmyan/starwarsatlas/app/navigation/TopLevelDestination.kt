package com.okmyan.starwarsatlas.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.ui.graphics.vector.ImageVector
import com.okmyan.starwarsatlas.R

sealed class TopLevelDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    data object People : TopLevelDestination(
        route = "people",
        labelRes = R.string.nav_people,
        icon = Icons.Outlined.Groups,
    )

    data object Starships : TopLevelDestination(
        route = "starships",
        labelRes = R.string.nav_starships,
        icon = Icons.Outlined.Rocket,
    )

    data object Planets : TopLevelDestination(
        route = "planets",
        labelRes = R.string.nav_planets,
        icon = Icons.Outlined.Language,
    )
}
