package com.okmyan.starwarsatlas.feature.planets.presentation

import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
import com.okmyan.starwarsatlas.feature.planets.data.PlanetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlanetsListViewModel @Inject constructor(
    private val planetsRepository: PlanetsRepository,
) : BaseViewModel<PlanetsListState>(PlanetsListState()) {

    init {
        loadPlanets()
    }

    fun loadPlanets() {
        scope.launch(CoroutineName("PlanetsListViewModel - loadPlanets")) {
            Timber.d("Requesting planets list")
            updateState {
                copy(isLoading = true, error = null)
            }

            when (val outcome = planetsRepository.getPlanets()) {
                is Outcome.Success -> {
                    Timber.d("Planets list loaded: ${outcome.value.size} items")
                    updateState {
                        copy(
                            isLoading = false,
                            items = outcome.value,
                        )
                    }
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load planets list: ${outcome.error}")
                    updateState {
                        copy(
                            isLoading = false,
                            items = emptyList(),
                            error = outcome.error,
                        )
                    }
                }
            }
        }
    }
}
