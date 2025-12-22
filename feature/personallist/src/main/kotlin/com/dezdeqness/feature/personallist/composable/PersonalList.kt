package com.dezdeqness.feature.personallist.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.personallist.PersonalListAction
import com.dezdeqness.feature.personallist.model.UserRateUiModel

@Composable
fun PersonalList(
    modifier: Modifier = Modifier,
    list: List<UserRateUiModel>,
    isScrollNeed: Boolean,
    onNeedScroll: (LazyListState) -> Unit,
    onActionReceive: (PersonalListAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(isScrollNeed) {
        if (isScrollNeed) {
            onNeedScroll(listState)
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        items(
            count = list.size,
            key = { index -> list[index].id + list[index].name.hashCode() },
        ) { index ->
            PersonalListAnimeItem(
                userRateUiModel = list[index],
                modifier = Modifier
                    .animateItem()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    )
                ,
                onActionReceive = onActionReceive,
            )
        }
    }
}
