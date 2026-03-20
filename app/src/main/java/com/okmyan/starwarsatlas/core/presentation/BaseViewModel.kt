package com.okmyan.starwarsatlas.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.utils.coroutines.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus

abstract class BaseViewModel<T>(initialState: T) : ViewModel() {

    protected val scope = viewModelScope +
            CoroutineName(this::class.simpleName.toString()) +
            coroutineExceptionHandler()

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    protected fun updateState(transform: T.() -> T) = _state.update(transform)

}
