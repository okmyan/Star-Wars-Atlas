package com.okmyan.starwarsatlas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.okmyan.starwarsatlas.core.ui.theme.StarWarsAtlasTheme
import com.okmyan.starwarsatlas.feature.people.presentation.PeopleListScreen
import com.okmyan.starwarsatlas.feature.planets.presentation.PlanetsListScreen
import com.okmyan.starwarsatlas.feature.starships.presentation.StarshipsListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StarWarsAtlasTheme {
                PlanetsListScreen()
//                StarshipsListScreen()
//                PeopleListScreen()
            }
        }
    }
}
