package com.dezdeqness.feature.onboarding.selectgenres.presentation.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.chips.AppChip
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreChip(
    modifier: Modifier = Modifier,
    genre: GenreUiModel,
    onSelected: () -> Unit,
    isSelected: Boolean,
) {
    val titleColor = if (isSelected) {
        Color.White
    } else {
        AppTheme.colors.textPrimary
    }

    val backgroundColor = if (isSelected) {
        AppTheme.colors.primary
    } else {
        AppTheme.colors.onPrimary
    }

    AppChip(
        modifier = modifier,
        title = genre.name,
        titleColor = titleColor,
        onClick = onSelected,
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = backgroundColor,
            selectedContainerColor = backgroundColor
        )
    )
}
