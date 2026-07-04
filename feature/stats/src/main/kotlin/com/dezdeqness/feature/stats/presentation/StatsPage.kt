package com.dezdeqness.feature.stats.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.stats.R
import com.dezdeqness.feature.stats.presentation.composables.DiagramChart
import com.dezdeqness.feature.stats.presentation.composables.GenericHeader
import com.dezdeqness.feature.stats.presentation.composables.HorizontalChart
import com.dezdeqness.feature.stats.presentation.models.DiagramChartUiModel
import com.dezdeqness.feature.stats.presentation.models.ScoreChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsItem
import com.dezdeqness.feature.stats.presentation.preview.StatsPreviewData
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPage(
    modifier: Modifier = Modifier,
    state: StateFlow<StatsState>,
    onBackPressed: () -> Unit,
) {
    val statsState by state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            AppToolbar(
                title = stringResource(R.string.stats_toolbar_title),
                navigationClick = onBackPressed,
            )
        },
        containerColor = AppTheme.colors.onPrimary,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(statsState.items.size) { index ->
                val item = statsState.items[index]

                when (item) {
                    is StatsHeaderUiModel -> {
                        GenericHeader(
                            modifier = Modifier.padding(top = 12.dp),
                            title = item.header,
                        )
                    }

                    is DiagramChartUiModel -> {
                        DiagramChart(
                            chartData = item.items,
                            totalProgress = item.maxProgress,
                        )
                    }

                    is ScoreChartUiModel -> {
                        HorizontalChart(
                            maxProgress = item.maxProgress,
                            items = item.items,
                        )
                    }

                    is StatsChartUiModel -> {
                        HorizontalChart(
                            maxProgress = item.maxProgress,
                            items = item.items,
                        )
                    }
                }
            }

            item { Box(modifier = Modifier.height(40.dp)) }
        }
    }
}

data class StatsState(
    val items: List<StatsItem> = listOf(),
)

@PreviewLightDark
@Composable
fun StatsPagePreview() {
    AppTheme {
        StatsPage(
            state = MutableStateFlow(StatsState(items = StatsPreviewData.items)),
            onBackPressed = {},
        )
    }
}
