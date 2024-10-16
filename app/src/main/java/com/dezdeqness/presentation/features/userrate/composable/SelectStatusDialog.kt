package com.dezdeqness.presentation.features.userrate.composable

import android.content.Context
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
import com.dezdeqness.domain.model.UserRateStatusEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectStatusDialog(
    state: SheetState,
    statuses: List<SelectStatusItem>,
    onSelectedItem: (String) -> Unit,
    onCloseClicked: () -> Unit,
    selectedId: String,
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
                text = stringResource(R.string.select_status_dialog_title),
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
                        text = item.title,
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

data class SelectStatusItem(
    val id: String,
    val title: String,
) {
    companion object {
        fun composeList(context: Context) =
            listOf(
                SelectStatusItem(
                    id = UserRateStatusEntity.WATCHING.status,
                    title = context.getString(R.string.edit_rate_status_watching)
                ),
                SelectStatusItem(
                    id = UserRateStatusEntity.PLANNED.status,
                    title = context.getString(R.string.edit_rate_status_planned)
                ),
                SelectStatusItem(
                    id = UserRateStatusEntity.REWATCHING.status,
                    title = context.getString(R.string.edit_rate_status_rewatching)
                ),
                SelectStatusItem(
                    id = UserRateStatusEntity.COMPLETED.status,
                    title = context.getString(R.string.edit_rate_status_completed)
                ),
                SelectStatusItem(
                    id = UserRateStatusEntity.ON_HOLD.status,
                    title = context.getString(R.string.edit_rate_status_on_hold)
                ),
                SelectStatusItem(
                    id = UserRateStatusEntity.DROPPED.status,
                    title = context.getString(R.string.edit_rate_status_dropped)
                )
            )
    }
}
