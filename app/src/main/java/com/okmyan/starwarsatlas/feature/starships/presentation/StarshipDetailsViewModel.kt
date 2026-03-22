package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.okmyan.starwarsatlas.app.navigation.StarshipDetails
import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
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
) : BaseViewModel<StarshipDetailsState>(StarshipDetailsState.Loading) {

    private val starshipId: String = savedStateHandle.toRoute<StarshipDetails>().starshipId

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    private fun loadDetails() {
        Timber.d("Loading starship details for $starshipId")

        updateState { StarshipDetailsState.Loading }

        scope.launch(CoroutineName("StarshipDetailsViewModel - loadDetails")) {
            val result = repository.getStarshipDetails(starshipId)
            Timber.d("Starship details loaded: $result")

            updateState {
                when (result) {
                    is Outcome.Success -> StarshipDetailsState.Success(result.value)
                    is Outcome.Failure -> StarshipDetailsState.Error(result.error)
                }
            }
        }
    }
}
