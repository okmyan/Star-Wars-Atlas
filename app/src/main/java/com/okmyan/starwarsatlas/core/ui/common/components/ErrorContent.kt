package com.okmyan.starwarsatlas.core.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.okmyan.starwarsatlas.R
import com.okmyan.starwarsatlas.core.model.DataError

@Composable
fun ErrorContent(
    error: DataError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val errorMessage = when (error) {
        is DataError.NoInternetConnection -> stringResource(R.string.error_no_internet)
        is DataError.Timeout -> stringResource(R.string.error_timeout)

        is DataError.GraphQl,
        is DataError.Unknown -> stringResource(R.string.error_generic)
    }
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}
