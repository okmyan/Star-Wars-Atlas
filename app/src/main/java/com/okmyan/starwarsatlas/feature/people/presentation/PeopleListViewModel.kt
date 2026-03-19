package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import timber.log.Timber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )

            runCatching {
                peopleRepository.getPeople()
            }.onSuccess { people ->
                Timber.d("People list loaded: ${people.size} items")
                _uiState.value = PeopleListState(
                    isLoading = false,
                    items = people,
                    errorMessage = null,
                )
            }.onFailure { throwable ->
                Timber.e(throwable, "Failed to load people list")
                _uiState.value = PeopleListState(
                    isLoading = false,
                    items = emptyList(),
                    errorMessage = throwable.message ?: "Unknown error",
                )
            }
        }
    }
}
