package com.dezdeqness.presentation.features.personallist.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.RibbonStatusUiModel

@Composable
fun PersonalRibbon(
    modifier: Modifier = Modifier,
    items: List<RibbonStatusUiModel> = listOf(),
    selectedRibbonId: String = "",
    onClick: (String) -> Unit,
) {
    LazyRow(modifier = modifier) {
        items(
            count = items.size,
            key = { index -> items[index].id },
        ) { index ->
            val item = items[index]
            val isSelected = item.id == selectedRibbonId

            val localModifier = if (isSelected) {
                Modifier.background(color = colorResource(R.color.purple_500))
            } else {
                Modifier.border(
                    border = BorderStroke(1.dp, colorResource(R.color.filter_divider)),
                    shape = AppTheme.shapes.large,
                )
            }

            val textColor = if (isSelected) {
                colorResource(R.color.white)
            } else {
                AppTheme.colors.textPrimary
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(AppTheme.shapes.large)
                    .clickable(
                        onClick = {
                            onClick(item.id)
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = AppTheme.colors.ripple),
                    )
                    .then(localModifier)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    item.displayName,
                    modifier = Modifier,
                    color = textColor,
                )
            }
        }
    }
}

@Preview
@Composable
fun PersonalRibbonPreview() {
    AppTheme {
        PersonalRibbon(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.onPrimary)
                .padding(16.dp),
            items = listOf(
                RibbonStatusUiModel("key1", "Planned"),
                RibbonStatusUiModel("key2", "Watching"),
            ),
            selectedRibbonId = "key2",
            onClick = {},
        )
    }
}
