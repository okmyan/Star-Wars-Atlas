package com.okmyan.starwarsatlas.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.okmyan.starwarsatlas.feature.people.data.PeopleRepository
import com.okmyan.starwarsatlas.feature.people.domain.PersonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    peopleRepository: PeopleRepository,
) : ViewModel() {

    val items: Flow<PagingData<PersonListItem>> = peopleRepository.getPeople()
        .cachedIn(viewModelScope)

}
