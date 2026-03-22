package com.okmyan.starwarsatlas.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import timber.log.Timber

@Composable
fun StarWarsAtlasRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavControllerLogger(navController)

    val destinations = listOf(
        TopLevelDestination.People,
        TopLevelDestination.Starships,
        TopLevelDestination.Planets,
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    val selected = backStackEntry?.destination?.hierarchy
                        ?.any { it.hasRoute(destination.graph::class) } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.graph) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.labelRes),
                            )
                        },
                        label = {
                            Text(stringResource(destination.labelRes))
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        StarWarsAtlasNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun NavControllerLogger(navController: NavController) {
    DisposableEffect(navController) {
        var previousRoute = "none"
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val to = destination.route ?: "unknown"
            Timber.tag("NAVIGATION").d("$previousRoute -> $to")
            previousRoute = to
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }
}
