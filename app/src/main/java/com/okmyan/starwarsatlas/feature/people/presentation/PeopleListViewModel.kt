package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
) : ViewModel() {

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    val items: Flow<PagingData<PersonListItem>> = peopleRepository.getPeople()
        .cachedIn(viewModelScope)

    val favoritePeople: StateFlow<List<PersonListItem>> = peopleRepository.observeFavoritePeople()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggleFavoriteFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun toggleFavorite(personId: String) {
        viewModelScope.launch(CoroutineName("PeopleListViewModel - toggleFavorite for $personId")) {
            peopleRepository.toggleFavorite(personId)
        }
    }

}
