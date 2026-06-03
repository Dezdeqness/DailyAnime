package com.dezdeqness.feature.personallist.search.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.personallist.search.PersonalListTab
import com.dezdeqness.foundation.ui.theme.AppTheme

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
        indicator = {},
    ) {
        items.forEachIndexed { index, tab ->
            val isSelected = targetPage == index

            val localModifier = if (isSelected) {
                Modifier.background(AppTheme.colors.primary)
            } else {
                Modifier.border(
                    border = BorderStroke(1.dp, AppTheme.colors.surface),
                    shape = AppTheme.shapes.large,
                )
            }

            val textColor = if (isSelected) {
                Color.White
            } else {
                AppTheme.colors.textPrimary
            }

            val paddingStart = if (index == 0) 0.dp else 4.dp
            val paddingEnd = if (index < items.size - 1) 4.dp else 0.dp

            Box(
                modifier = Modifier
                    .padding(start = paddingStart, end = paddingEnd)
                    .clip(AppTheme.shapes.large)
                    .clickable(
                        onClick = {
                            onClick(index)
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = AppTheme.colors.ripple),
                    )
                    .then(localModifier)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    stringResource(tab.displayNameRes),
                    style = AppTheme.typography.bodyMedium,
                    modifier = Modifier,
                    color = textColor,
                )
            }
        }
    }
}

@Preview
@Composable
fun PersonalListTabsPreview() {
    AppTheme {
        PersonalListTabs(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.onPrimary)
                .padding(16.dp),
            items = listOf(
                PersonalListTab.All,
                PersonalListTab.Completed,
                PersonalListTab.Watching,
            ),
            targetPage = 0,
            onClick = {},
        )
    }
}
