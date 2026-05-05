package com.okmyan.starwarsatlas.feature.planets.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.okmyan.starwarsatlas.app.navigation.PlanetDetails
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.StatefulBaseViewModel
import com.okmyan.starwarsatlas.feature.planets.data.PlanetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlanetDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PlanetsRepository,
) : StatefulBaseViewModel<PlanetDetailsState>(PlanetDetailsState()) {

    private val planetId: String = savedStateHandle.toRoute<PlanetDetails>().planetId

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    private fun loadDetails() {
        Timber.d("Loading planet details for $planetId")

        updateState { copy(isLoading = true, error = null) }

        scope.launch(CoroutineName("PlanetDetailsViewModel - loadDetails for $planetId")) {
            val result = repository.getPlanetDetails(planetId)
            Timber.d("Planet details loaded: $result")

            updateState {
                when (result) {
                    is Outcome.Success -> copy(
                        isLoading = false,
                        error = null,
                        planet = result.value,
                    )

                    is Outcome.Failure -> copy(
                        isLoading = false,
                        error = result.error,
                    )
                }
            }
        }
    }
}
