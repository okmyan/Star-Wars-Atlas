package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarshipsListViewModel @Inject constructor(
    private val starshipsRepository: StarshipsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StarshipsListState(isLoading = true))
    val uiState: StateFlow<StarshipsListState> = _uiState.asStateFlow()

    init {
        loadStarships()
    }

    fun loadStarships() {
        viewModelScope.launch(CoroutineName("StarshipsListViewModel - loadStarships")) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )

            runCatching {
                starshipsRepository.getStarships()
            }.onSuccess { starships ->
                _uiState.value = StarshipsListState(
                    isLoading = false,
                    items = starships,
                    errorMessage = null,
                )
            }.onFailure { throwable ->
                _uiState.value = StarshipsListState(
                    isLoading = false,
                    items = emptyList(),
                    errorMessage = throwable.message ?: "Unknown error",
                )
            }
        }
    }
}
