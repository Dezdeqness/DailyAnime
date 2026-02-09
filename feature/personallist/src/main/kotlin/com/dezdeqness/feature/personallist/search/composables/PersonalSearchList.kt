package com.dezdeqness.feature.personallist.search.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.personallist.search.model.SearchUserRateUiModel

@Composable
fun PersonalSearchList(
    modifier: Modifier = Modifier,
    list: List<SearchUserRateUiModel>,
    onAnimeClick: (Long, String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = list.size,
            key = { index -> list[index].id },
        ) { index ->
            val item = list[index]
            PersonalListSearchItem(
                model = item,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                onAnimeClick = onAnimeClick,
            )
        }
    }
}
