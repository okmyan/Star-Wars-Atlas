package com.okmyan.starwarsatlas.feature.planets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.okmyan.starwarsatlas.feature.planets.data.PlanetsRepository
import com.okmyan.starwarsatlas.feature.planets.domain.PlanetListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PlanetsListViewModel @Inject constructor(
    planetsRepository: PlanetsRepository,
) : ViewModel() {

    val items: Flow<PagingData<PlanetListItem>> = planetsRepository.getPlanets()
        .cachedIn(viewModelScope)

}
