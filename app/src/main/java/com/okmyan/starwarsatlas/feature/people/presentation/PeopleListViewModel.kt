package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PeopleListState(isLoading = true))
    val uiState: StateFlow<PeopleListState> = _uiState.asStateFlow()

    init {
        loadPeople()
    }

    fun loadPeople() {
        viewModelScope.launch(CoroutineName("PeopleListViewModel - loadPeople")) {
            Timber.d("Requesting people list")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val outcome = peopleRepository.getPeople()) {
                is Outcome.Success -> {
                    Timber.d("People list loaded: ${outcome.value.size} items")
                    _uiState.value = PeopleListState(
                        isLoading = false,
                        items = outcome.value,
                    )
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load people list: ${outcome.error}")
                    _uiState.value = PeopleListState(
                        isLoading = false,
                        error = outcome.error,
                    )
                }
            }
        }
    }
}
