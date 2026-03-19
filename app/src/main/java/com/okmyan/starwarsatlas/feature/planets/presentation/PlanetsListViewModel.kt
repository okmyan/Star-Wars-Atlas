package com.okmyan.starwarsatlas.feature.planets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.feature.planets.data.PlanetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import timber.log.Timber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )

            runCatching {
                planetsRepository.getPlanets()
            }.onSuccess { planets ->
                Timber.d("Planets list loaded: ${planets.size} items")
                _uiState.value = PlanetsListState(
                    isLoading = false,
                    items = planets,
                    errorMessage = null,
                )
            }.onFailure { throwable ->
                Timber.e(throwable, "Failed to load planets list")
                _uiState.value = PlanetsListState(
                    isLoading = false,
                    items = emptyList(),
                    errorMessage = throwable.message ?: "Unknown error",
                )
            }
        }
    }
}
