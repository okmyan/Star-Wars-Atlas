package com.okmyan.starwarsatlas.core.ui.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.okmyan.starwarsatlas.R
import com.okmyan.starwarsatlas.core.model.DataError

@Composable
fun DataError.toStringResource() = when (this) {
    is DataError.NoInternetConnection -> stringResource(R.string.error_no_internet)
    is DataError.Timeout -> stringResource(R.string.error_timeout)

    is DataError.GraphQl,
    is DataError.Unknown -> stringResource(R.string.error_generic)
}
