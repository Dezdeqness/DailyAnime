package com.dezdeqness.presentation.features.animedetails.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsToolbar(
    modifier: Modifier = Modifier,
    toolbarColor: Color = AppTheme.colors.onPrimary,
    isLoaded: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    AppToolbar(
        modifier = modifier,
        title = "",
        navigationClick = onBackClick,
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = toolbarColor),
        actions = {
            if (isLoaded) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface,
                    )
                }
            }
        },
    )
}
