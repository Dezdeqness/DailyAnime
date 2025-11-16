package com.dezdeqness.feature.onboarding.selectgenres.presentation.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel

@Composable
fun SectionBlock(
    modifier: Modifier = Modifier,
    title: String,
    items: List<GenreUiModel>,
    selectedIds: Set<String>,
    onItemClick: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.titleMedium,
            )

            if (items.size > 8) {
                ExpandButton(expanded = expanded) {
                    expanded = !expanded
                }
            }
        }

        val visibleItems = if (expanded) {
            items
        } else {
            items.take(8)
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            visibleItems.forEach { item ->
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