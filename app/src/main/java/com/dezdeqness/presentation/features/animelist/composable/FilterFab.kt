package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun FilterFab(
    isFilterApplied: Boolean,
    onFilterClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(
            visible = isFilterApplied,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            SmallFloatingActionButton(
                containerColor = AppTheme.colors.primary,
                contentColor = AppTheme.colors.white,
                onClick = { onClearClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = AppTheme.colors.white,
                )
            }
        }

        FloatingActionButton(
            containerColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.white,
            onClick = { onFilterClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter_list),
                contentDescription = null,
                tint = AppTheme.colors.white,
            )
        }
    }
}