package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.BriefInfoUiModel
import com.google.common.collect.ImmutableList

@Composable
fun DetailsMoreInfo(
    modifier: Modifier = Modifier,
    moreInfoList: ImmutableList<BriefInfoUiModel>,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(moreInfoList.size) { index ->
            val item = moreInfoList[index]

            BriefInfoItem(item = item)
        }
    }
}

@Composable
private fun BriefInfoItem(modifier: Modifier = Modifier, item: BriefInfoUiModel) {
    Column(modifier = modifier) {
        Text(
            text = item.title,
            style = AppTheme.typography.labelMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = AppTheme.colors.textPrimary.copy(alpha = 0.9f),
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
            text = item.info,
            style = AppTheme.typography.labelMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            color = AppTheme.colors.textPrimary.copy(alpha = 0.9f),
        )
    }
}
