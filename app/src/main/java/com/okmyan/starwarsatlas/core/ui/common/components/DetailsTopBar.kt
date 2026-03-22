package com.okmyan.starwarsatlas.core.ui.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.okmyan.starwarsatlas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    name: String?,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(name.orEmpty())
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.details_back),
                )
            }
        },
    )
}
