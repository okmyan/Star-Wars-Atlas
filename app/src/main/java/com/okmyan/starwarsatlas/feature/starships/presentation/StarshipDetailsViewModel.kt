package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.okmyan.starwarsatlas.app.navigation.StarshipDetails
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.StatefulBaseViewModel
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StarshipDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: StarshipsRepository,
) : StatefulBaseViewModel<StarshipDetailsState>(StarshipDetailsState()) {

    private val starshipId: String = savedStateHandle.toRoute<StarshipDetails>().starshipId

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    private fun loadDetails() {
        Timber.d("Loading starship details for $starshipId")

        updateState { copy(isLoading = true, error = null) }

        scope.launch(CoroutineName("StarshipDetailsViewModel - loadDetails")) {
            val result = repository.getStarshipDetails(starshipId)
            Timber.d("Starship details loaded: $result")

            updateState {
                when (result) {
                    is Outcome.Success -> copy(
                        isLoading = false,
                        error = null,
                        starship = result.value,
                    )

                    is Outcome.Failure -> copy(
                        isLoading = false,
                        error = result.error,
                    )
                }
            }
        }
    }
}
