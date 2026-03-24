package com.okmyan.starwarsatlas.feature.starships.presentation

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.okmyan.starwarsatlas.core.presentation.BaseViewModel
import com.okmyan.starwarsatlas.feature.starships.data.StarshipsRepository
import com.okmyan.starwarsatlas.feature.starships.domain.StarshipListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StarshipsListViewModel @Inject constructor(
    starshipsRepository: StarshipsRepository,
) : BaseViewModel() {

    val items: Flow<PagingData<StarshipListItem>> = starshipsRepository.getStarships()
        .cachedIn(scope)

}
