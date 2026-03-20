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
import androidx.compose.ui.unit.dp
import com.okmyan.starwarsatlas.core.model.DataError

@Composable
fun ErrorContent(
    error: DataError,
    onRetry: () -> Unit,
) {
    val errorMessage = when (error) {
        is DataError.NoInternetConnection -> "No internet connection"
        is DataError.Timeout -> "Request timed out. Please try again"
        is DataError.GraphQl -> error.message
        is DataError.Unknown -> "Something went wrong. Please try again"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
