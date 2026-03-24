package com.okmyan.starwarsatlas.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.starwarsatlas.utils.coroutines.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {

    protected val scope = viewModelScope +
            CoroutineName(this::class.simpleName.toString()) +
            coroutineExceptionHandler()

}
