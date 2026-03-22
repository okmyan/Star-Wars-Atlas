package com.okmyan.starwarsatlas.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.okmyan.starwarsatlas.feature.people.presentation.PeopleListScreen
import com.okmyan.starwarsatlas.feature.people.presentation.PersonDetailsScreen
import com.okmyan.starwarsatlas.feature.planets.presentation.PlanetDetailsScreen
import com.okmyan.starwarsatlas.feature.planets.presentation.PlanetsListScreen
import com.okmyan.starwarsatlas.feature.starships.presentation.StarshipDetailsScreen
import com.okmyan.starwarsatlas.feature.starships.presentation.StarshipsListScreen

@Composable
fun StarWarsAtlasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = PeopleGraph,
        modifier = modifier,
    ) {
        navigation<PeopleGraph>(startDestination = PeopleList) {
            composable<PeopleList> {
                PeopleListScreen(
                    onPersonClick = { id -> navController.navigate(PersonDetails(id)) },
                )
            }

            composable<PersonDetails> {
                PersonDetailsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }

        navigation<StarshipsGraph>(startDestination = StarshipsList) {
            composable<StarshipsList> {
                BackHandler { navController.navigateBackToPeople() }
                StarshipsListScreen(
                    onStarshipClick = { id -> navController.navigate(StarshipDetails(id)) },
                )
            }

            composable<StarshipDetails> {
                StarshipDetailsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }

        navigation<PlanetsGraph>(startDestination = PlanetsList) {
            composable<PlanetsList> {
                BackHandler { navController.navigateBackToPeople() }
                PlanetsListScreen(
                    onPlanetClick = { id -> navController.navigate(PlanetDetails(id)) },
                )
            }

            composable<PlanetDetails> {
                PlanetDetailsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

/**
 * Registered inside NavHost so it has higher priority than NavHost's own back handler.
 * Pressing back restores the [PeopleGraph] state including any detail screen that was open before
 * switching tabs.
 */
private fun NavHostController.navigateBackToPeople() {
    navigate(PeopleGraph) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
