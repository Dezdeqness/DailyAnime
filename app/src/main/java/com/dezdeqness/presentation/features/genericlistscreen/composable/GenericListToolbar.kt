package com.dezdeqness.presentation.features.genericlistscreen.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericListToolbar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                title,
                color = AppTheme.colors.textPrimary,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = AppTheme.colors.onSecondary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.background_tint)
        ),
        modifier = modifier,
    )
}
