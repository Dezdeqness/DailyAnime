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
    selectedTab: PersonalListTab,
    onTabSelected: (PersonalListTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = PersonalListTab.entries

    SecondaryScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        edgePadding = 16.dp,
        divider = {},
    ) {
        tabs.forEachIndexed { _, tab ->
            val isSelected = selectedTab == tab

            Tab(
                selected = isSelected,
                onClick = {
                    onTabSelected(tab)
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
