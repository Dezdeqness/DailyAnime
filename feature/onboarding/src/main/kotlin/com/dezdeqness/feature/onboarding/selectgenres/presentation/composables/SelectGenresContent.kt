package com.dezdeqness.feature.onboarding.selectgenres.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel

@Composable
fun SelectGenresContent(
    list: List<GenreUiModel>,
    selectedIds: Set<String>,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxSelection = 3

    val genres = remember { list.filter { it.isGenre } }
    val themes = remember { list.filter { !it.isGenre } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Выберите интересы",
                style = AppTheme.typography.headlineMedium,
                color = AppTheme.colors.textPrimary,
            )

            Box(
                modifier = Modifier
                    .background(
                        if (selectedIds.size >= maxSelection)
                            AppTheme.colors.error.copy(alpha = 0.15f)
                        else
                            AppTheme.colors.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${selectedIds.size} / $maxSelection",
                    style = AppTheme.typography.bodySmall.copy(
                        color = if (selectedIds.size >= maxSelection)
                            AppTheme.colors.error
                        else
                            AppTheme.colors.primary
                    )
                )
            }
        }

        SectionBlock(
            title = "Жанры",
            items = genres,
            selectedIds = selectedIds,
            onItemClick = onGenreClick
        )

        SectionBlock(
            title = "Темы",
            items = themes,
            selectedIds = selectedIds,
            onItemClick = onGenreClick
        )

    }
}

@Composable
private fun SectionBlock(
    modifier: Modifier = Modifier,
    title: String,
    items: List<GenreUiModel>,
    selectedIds: Set<String>,
    onItemClick: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.titleMedium,
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                val isSelected = selectedIds.contains(item.id)

                GenreChip(
                    genre = item,
                    onSelected = { onItemClick(item.id) },
                    isSelected = isSelected,
                )
            }
        }
    }
}
