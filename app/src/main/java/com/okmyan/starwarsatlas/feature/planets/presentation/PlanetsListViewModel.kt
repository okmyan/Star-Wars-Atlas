package com.okmyan.starwarsatlas.feature.planets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.planets.data.PlanetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlanetsListViewModel @Inject constructor(
    private val planetsRepository: PlanetsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlanetsListState(isLoading = true))
    val uiState: StateFlow<PlanetsListState> = _uiState.asStateFlow()

    init {
        loadPlanets()
    }

    fun loadPlanets() {
        viewModelScope.launch(CoroutineName("PlanetsListViewModel - loadPlanets")) {
            Timber.d("Requesting planets list")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val outcome = planetsRepository.getPlanets()) {
                is Outcome.Success -> {
                    Timber.d("Planets list loaded: ${outcome.value.size} items")
                    _uiState.value = PlanetsListState(
                        isLoading = false,
                        items = outcome.value,
                    )
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load planets list: ${outcome.error}")
                    _uiState.value = PlanetsListState(
                        isLoading = false,
                        error = outcome.error,
                    )
                }
            }
        }
    }
}
