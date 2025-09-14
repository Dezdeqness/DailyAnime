package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.feature.history.presentation.composables.HistoryItem
import com.dezdeqness.feature.history.presentation.models.HistoryModel
import com.dezdeqness.presentation.action.Action

@Composable
fun HistoryLatestSection(
    modifier: Modifier = Modifier,
    item: HistoryModel.HistoryUiModel,
    onActionReceive: (Action) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Header(
            title = stringResource(R.string.section_history_title),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onActionReceive.invoke(Action.HistoryHeaderClicked)
            },
        )
        HistoryItem(
            item = item,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

}
