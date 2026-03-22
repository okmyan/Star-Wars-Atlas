package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StarshipsListViewModel @Inject constructor(
    starshipsRepository: StarshipsRepository,
) : ViewModel() {

    val items: Flow<PagingData<StarshipListItem>> = starshipsRepository.getStarships()
        .cachedIn(viewModelScope)

}
