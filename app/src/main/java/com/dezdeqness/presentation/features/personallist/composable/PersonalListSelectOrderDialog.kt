package com.dezdeqness.presentation.features.personallist.composable

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.domain.model.Sort

@Composable
fun PersonalListSelectOrderDialog(
    modifier: Modifier = Modifier,
    onSelectedItem: (String) -> Unit,
    selectedId: String,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current

    val listStatuses = remember {
        SelectOrderItem.composeList(context)
    }

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults
                .cardColors()
                .copy(containerColor = AppTheme.colors.onPrimary),
        ) {

            Column(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(
                        text = stringResource(R.string.select_status_dialog_title),
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        style = AppTheme.typography.titleMedium,
                        color = AppTheme.colors.textPrimary,
                    )

                    listStatuses.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    onClick = {
                                        onSelectedItem(item.id)
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


                }
            }
        }
    }

}

data class SelectOrderItem(
    val id: String,
    val title: String,
) {
    companion object {
        fun composeList(context: Context) =
            listOf(
                SelectOrderItem(
                    id = Sort.NAME.sort,
                    title = context.getString(R.string.personal_list_select_order_name)
                ),
                SelectOrderItem(
                    id = Sort.PROGRESS.sort,
                    title = context.getString(R.string.personal_list_select_order_progress)
                ),
                SelectOrderItem(
                    id = Sort.SCORE.sort,
                    title = context.getString(R.string.personal_list_select_order_score)
                ),
                SelectOrderItem(
                    id = Sort.EPISODES.sort,
                    title = context.getString(R.string.personal_list_select_order_episodes)
                ),
            )
    }
}
