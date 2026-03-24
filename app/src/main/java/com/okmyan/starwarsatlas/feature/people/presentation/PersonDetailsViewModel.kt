package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.okmyan.starwarsatlas.app.navigation.PersonDetails
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PeopleRepository,
) : BaseViewModel<PersonDetailsState>(PersonDetailsState.Loading) {

    private val personId: String = savedStateHandle.toRoute<PersonDetails>().personId

    val isFavorite: StateFlow<Boolean> = repository.observeFavoriteById(personId)
        .stateIn(scope, SharingStarted.Eagerly, false)

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    fun toggleFavorite() {
        scope.launch(CoroutineName("PersonDetailsViewModel - toggleFavorite")) {
            repository.toggleFavorite(personId)
        }
    }

    private fun loadDetails() {
        Timber.d("Loading person details for $personId")

        updateState { PersonDetailsState.Loading }

        scope.launch(CoroutineName("PersonDetailsViewModel - loadDetails")) {
            val result = repository.getPersonDetails(personId)
            Timber.d("Person details loaded: $result")

            updateState {
                when (result) {
                    is Outcome.Success -> PersonDetailsState.Success(result.value)
                    is Outcome.Failure -> PersonDetailsState.Error(result.error)
                }
            }
        }
    }
}
