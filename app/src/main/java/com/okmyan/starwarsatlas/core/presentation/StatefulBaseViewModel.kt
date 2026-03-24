package com.okmyan.starwarsatlas.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class StatefulBaseViewModel<T>(initialState: T) : BaseViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    protected fun updateState(transform: T.() -> T) = _state.update(transform)

}
