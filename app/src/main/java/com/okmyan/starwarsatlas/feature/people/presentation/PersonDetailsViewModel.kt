package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.okmyan.starwarsatlas.app.navigation.PersonDetails
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.StatefulBaseViewModel
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PeopleRepository,
) : StatefulBaseViewModel<PersonDetailsState>(PersonDetailsState()) {

    private val personId: String = savedStateHandle.toRoute<PersonDetails>().personId

    init {
        observeFavorite()
        loadDetails()
    }

    fun retry() = loadDetails()

    fun toggleFavorite() {
        scope.launch(CoroutineName("PersonDetailsViewModel - toggleFavorite")) {
            repository.toggleFavorite(personId)
        }
    }

    private fun observeFavorite() {
        scope.launch(CoroutineName("PersonDetailsViewModel - observeFavorite by $personId")) {
            repository.observeFavoriteById(personId).collect { isFavorite ->
                updateState { copy(isFavorite = isFavorite) }
            }
        }
    }

    private fun loadDetails() {
        Timber.d("Loading person details for $personId")

        updateState { copy(isLoading = true, error = null) }

        scope.launch(CoroutineName("PersonDetailsViewModel - loadDetails for $personId")) {
            val personDetails = repository.getPersonDetails(personId)
            Timber.d("Person details loaded: $personDetails")

            updateState {
                when (personDetails) {
                    is Outcome.Success -> copy(
                        person = personDetails.value,
                        isLoading = false,
                        error = null,
                    )

                    is Outcome.Failure -> copy(
                        isLoading = false,
                        error = personDetails.error,
                    )
                }
            }
        }
    }
}
