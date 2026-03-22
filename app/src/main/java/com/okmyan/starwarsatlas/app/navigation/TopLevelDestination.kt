package com.okmyan.starwarsatlas.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.ui.graphics.vector.ImageVector
import com.okmyan.starwarsatlas.R

sealed class TopLevelDestination<T : Any>(
    val graph: T,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    data object People : TopLevelDestination<PeopleGraph>(
        graph = PeopleGraph,
        labelRes = R.string.nav_people,
        icon = Icons.Outlined.Groups,
    )

    data object Starships : TopLevelDestination<StarshipsGraph>(
        graph = StarshipsGraph,
        labelRes = R.string.nav_starships,
        icon = Icons.Outlined.Rocket,
    )

    data object Planets : TopLevelDestination<PlanetsGraph>(
        graph = PlanetsGraph,
        labelRes = R.string.nav_planets,
        icon = Icons.Outlined.Language,
    )
}
