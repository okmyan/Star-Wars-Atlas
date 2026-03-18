package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
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
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )

            runCatching {
                peopleRepository.getPeople()
            }.onSuccess { people ->
                _uiState.value = PeopleListState(
                    isLoading = false,
                    items = people,
                    errorMessage = null,
                )
            }.onFailure { throwable ->
                _uiState.value = PeopleListState(
                    isLoading = false,
                    items = emptyList(),
                    errorMessage = throwable.message ?: "Unknown error",
                )
            }
        }
    }
}
