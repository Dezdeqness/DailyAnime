package com.dezdeqness.feature.home.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.history.presentation.composables.HistoryItem
import com.dezdeqness.feature.history.presentation.models.HistoryModel
import com.dezdeqness.feature.home.R
import com.dezdeqness.foundation.ui.views.header.Header

@Composable
fun HistoryLatestSection(
    modifier: Modifier = Modifier,
    item: HistoryModel.HistoryUiModel,
    onHistoryHeaderClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Header(
            title = stringResource(R.string.section_history_title),
            modifier = Modifier.fillMaxWidth(),
            onClick = onHistoryHeaderClicked,
        )
        HistoryItem(
            item = item,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
