package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
            Timber.d("Requesting starships list")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val outcome = starshipsRepository.getStarships()) {
                is Outcome.Success -> {
                    Timber.d("Starships list loaded: ${outcome.value.size} items")
                    _uiState.value = StarshipsListState(
                        isLoading = false,
                        items = outcome.value,
                    )
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load starships list: ${outcome.error}")
                    _uiState.value = StarshipsListState(
                        isLoading = false,
                        error = outcome.error,
                    )
                }
            }
        }
    }
}
