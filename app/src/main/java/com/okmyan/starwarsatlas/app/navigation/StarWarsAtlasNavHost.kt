package com.okmyan.starwarsatlas.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.okmyan.starwarsatlas.feature.people.presentation.PeopleListScreen
import com.okmyan.starwarsatlas.feature.planets.presentation.PlanetsListScreen
import com.okmyan.starwarsatlas.feature.starships.presentation.StarshipsListScreen

@Composable
fun StarWarsAtlasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.People.route,
        modifier = modifier,
    ) {
        composable(TopLevelDestination.People.route) {
            PeopleListScreen()
        }

        composable(TopLevelDestination.Starships.route) {
            StarshipsListScreen()
        }

        composable(TopLevelDestination.Planets.route) {
            PlanetsListScreen()
        }
    }
}
