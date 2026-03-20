package com.okmyan.starwarsatlas.feature.people.presentation

import com.okmyan.starwarsatlas.core.model.Outcome
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
) : BaseViewModel<PeopleListState>(PeopleListState()) {

    init {
        loadPeople()
    }

    fun loadPeople() {
        scope.launch(CoroutineName("PeopleListViewModel - loadPeople")) {
            Timber.d("Requesting people list")
            updateState {
                copy(isLoading = true, error = null)
            }

            when (val outcome = peopleRepository.getPeople()) {
                is Outcome.Success -> {
                    Timber.d("People list loaded: ${outcome.value.size} items")
                    updateState {
                        copy(
                            isLoading = false,
                            items = outcome.value,
                        )
                    }
                }

                is Outcome.Failure -> {
                    Timber.e("Failed to load people list: ${outcome.error}")
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
