package com.dezdeqness.feature.personallist.search.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.personallist.search.PersonalListTab

@Composable
fun PersonalListTabs(
    items: List<PersonalListTab>,
    targetPage: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = targetPage,
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        edgePadding = 16.dp,
        divider = {},
    ) {
        items.forEachIndexed { index, tab ->
            val isSelected = targetPage == index

            Tab(
                selected = isSelected,
                onClick = {
                    onClick(index)
                },
            ) {
                Text(
                    text = stringResource(tab.displayNameRes),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
