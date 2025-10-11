package com.dezdeqness.core.page

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
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.DiagramChart
import com.dezdeqness.core.ui.GenericHeader
import com.dezdeqness.core.ui.HorizontalChart
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.DiagramChartUiModel
import com.dezdeqness.presentation.models.ScoreChartUiModel
import com.dezdeqness.presentation.models.StatsChartUiModel
import com.dezdeqness.presentation.models.StatsHeaderUiModel
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
                            totalProgress = item.maxProgress
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

                    else -> {}
                }
            }

            item { Box(modifier = Modifier.height(40.dp)) }
        }
    }

}

data class StatsState(
    val items: List<AdapterItem> = listOf(),
)
