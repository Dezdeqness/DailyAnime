package com.dezdeqness.presentation.features.animedetails.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsHeader
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HeaderItemUiModel
import com.google.common.collect.ImmutableList

@Composable
fun DetailsList(
    modifier: Modifier = Modifier,
    list: ImmutableList<AdapterItem>,
) {
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id() + item.hashCode()
            }
        ) { index ->
            val item = list[index]

            when (item) {
                is HeaderItemUiModel -> {
                    DetailsHeader(detailsHeader = item)
                }
                else -> {
                }
            }
        }

    }
}
