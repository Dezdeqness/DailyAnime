package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.GeneralError
import com.dezdeqness.core.ui.GeneralLoading
import com.dezdeqness.presentation.action.Action

@Composable
fun HomeSection(
    modifier: Modifier = Modifier,
    title: String,
    items: List<SectionAnimeUiModel>,
    status: SectionStatus,
    onActionReceive: (Action) -> Unit,
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Box(
            modifier = modifier.wrapContentHeight()
        ) {
            when (status) {
                SectionStatus.Loading -> {
                    GeneralLoading(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }

                SectionStatus.Error -> {
                    GeneralError(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }

                else -> Unit
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier,
            ) {
                items(
                    count = items.size,
                    key = { index -> items[index].id },
                ) { index ->
                    val item = items[index]

                    val paddingStart = if (index == 0) 0.dp else 4.dp
                    val paddingEnd = if (index < items.size - 1) 4.dp else 0.dp

                    SectionAnimeItem(
                        modifier = Modifier.padding(start = paddingStart, end = paddingEnd),
                        item =item,
                        onClick = {
                            onActionReceive(Action.AnimeClick(animeId = item.id))
                        },
                    )
                }
            }
        }
    }
}
