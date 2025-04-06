package com.dezdeqness.presentation.features.animedetails.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsToolbar(
    modifier: Modifier = Modifier,
    toolbarColor: Color = AppTheme.colors.onPrimary,
    isLoaded: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = AppTheme.colors.onSecondary,
                )
            }
        },
        actions = {
            if (isLoaded) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null,
                        tint = AppTheme.colors.onSecondary,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = toolbarColor,
        ),
        modifier = modifier,
    )
}
