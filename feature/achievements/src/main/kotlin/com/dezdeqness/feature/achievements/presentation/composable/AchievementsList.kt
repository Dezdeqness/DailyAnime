package com.dezdeqness.feature.achievements.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.feature.achievements.R
import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel

@Composable
fun AchievementsList(
    modifier: Modifier = Modifier,
    common: List<AchievementsUiModel>,
    genres: List<AchievementsUiModel>,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Header(
                title = stringResource(R.string.title_common),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        items(
            count = common.size,
            key = { index -> common[index].id },
        ) { index ->
            val item = common[index]
            AchievementCell(
                item = item,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Header(
                title = stringResource(R.string.title_genres),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        items(
            count = genres.size,
            key = { index -> genres[index].id },
        ) { index ->
            val item = genres[index]
            AchievementCell(
                item = item,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
