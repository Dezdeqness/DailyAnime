package com.dezdeqness.presentation.features.settings.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSectionDialog(
    state: SheetState,
    statuses: List<SelectSectionItem>,
    onSelectedItem: (Int) -> Unit,
    onCloseClicked: () -> Unit,
    selectedId: Int,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onCloseClicked()
        },
        sheetState = state,
        containerColor = colorResource(id = R.color.background_tint)
    ) {

        Column {
            Text(
                text = stringResource(R.string.select_section_dialog_title),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textPrimary,
            )

            statuses.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            onClick = {
                                onSelectedItem(item.id)
                                onCloseClicked()
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = AppTheme.colors.ripple)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = item.id == selectedId,
                        onClick = null,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Text(
                        text = stringResource(item.titleId),
                        color = AppTheme.colors.textPrimary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            )
        }
    }
}

data class SelectSectionItem(
    val id: Int,
    @StringRes val titleId: Int,
) {
    companion object {
        fun getSections() =
            listOf(
                SelectSectionItem(
                    id = R.id.home_nav_graph,
                    titleId = R.string.bottom_navigation_home
                ),
                SelectSectionItem(
                    id = R.id.personal_host_nav_graph,
                    titleId = R.string.bottom_navigation_personal_lists
                ),
                SelectSectionItem(
                    id = R.id.calendar_nav_graph,
                    titleId = R.string.bottom_navigation_calendar
                ),
                SelectSectionItem(
                    id = R.id.profile_nav_graph,
                    titleId = R.string.bottom_navigation_profile
                ),
                SelectSectionItem(
                    id = R.id.search_nav_graph,
                    titleId = R.string.bottom_navigation_search
                ),
            )

        fun getById(sectionId: Int): SelectSectionItem{
            val items = getSections()

            return items.find { it.id == sectionId } ?: items.first()
        }
    }
}
