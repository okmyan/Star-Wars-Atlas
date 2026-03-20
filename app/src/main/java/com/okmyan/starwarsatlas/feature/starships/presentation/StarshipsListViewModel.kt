package com.okmyan.starwarsatlas.feature.starships.presentation

import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StarshipsListViewModel @Inject constructor(
    private val starshipsRepository: StarshipsRepository,
) : BaseViewModel<StarshipsListState>(StarshipsListState()) {

    init {
        loadStarships()
    }

    fun loadStarships() {
        scope.launch(CoroutineName("StarshipsListViewModel - loadStarships")) {
            Timber.d("Requesting starships list")
            updateState {
                copy(isLoading = true, error = null)
            }

            when (val outcome = starshipsRepository.getStarships()) {
                is Outcome.Success -> {
                    Timber.d("Starships list loaded: ${outcome.value.size} items")
                    updateState {
                        copy(
                            isLoading = false,
                            items = outcome.value,
                        )
                    }
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load starships list: ${outcome.error}")
                    updateState {
                        copy(
                            isLoading = false,
                            items = emptyList(),
                            error = outcome.error,
                        )
                    }
                }
            }
        }
    }
}
