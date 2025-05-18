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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.domain.model.InitialSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSectionDialog(
    state: SheetState,
    statuses: List<SelectSectionItem>,
    onSelectedItem: (InitialSection) -> Unit,
    onCloseClicked: () -> Unit,
    selectedId: Int,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onCloseClicked()
        },
        sheetState = state,
        containerColor = AppTheme.colors.onPrimary,
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
                                onSelectedItem(item.section)
                                onCloseClicked()
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = AppTheme.colors.ripple)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = item.section.id == selectedId,
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
    val section: InitialSection,
    @StringRes val titleId: Int,
) {
    companion object {
        fun getSections() =
            listOf(
                SelectSectionItem(
                    section = InitialSection.HOME,
                    titleId = R.string.bottom_navigation_home
                ),
                SelectSectionItem(
                    section = InitialSection.FAVORITES,
                    titleId = R.string.bottom_navigation_personal_lists
                ),
                SelectSectionItem(
                    section = InitialSection.CALENDAR,
                    titleId = R.string.bottom_navigation_calendar
                ),
                SelectSectionItem(
                    section = InitialSection.PROFILE,
                    titleId = R.string.bottom_navigation_profile
                ),
                SelectSectionItem(
                    section = InitialSection.SEARCH,
                    titleId = R.string.bottom_navigation_search
                ),
            )

        fun getById(sectionId: Int): SelectSectionItem{
            val items = getSections()

            return items.find { it.section.id == sectionId } ?: items.first()
        }
    }
}
