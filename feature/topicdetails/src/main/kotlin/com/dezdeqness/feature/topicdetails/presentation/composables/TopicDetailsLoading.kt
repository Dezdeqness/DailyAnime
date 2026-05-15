package com.dezdeqness.feature.topicdetails.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.shimmer

@Composable
fun TopicDetailsLoading(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.onPrimary),
                shape = AppTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    TopicDetailsUserInfoShimmer()
                    Spacer(modifier = Modifier.height(20.dp))
                    TopicDetailsTitleShimmer()
                    Spacer(modifier = Modifier.height(20.dp))
                    TopicDetailsBodyShimmer()
                    Spacer(modifier = Modifier.height(16.dp))
                    TopicDetailsQuoteShimmer()
                    Spacer(modifier = Modifier.height(16.dp))
                    TopicDetailsMediaShimmer()
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.28f)
                        .height(14.dp)
                        .shimmer(shape = AppTheme.shapes.small),
                )
                LinkedEntityShimmerCard()
            }
        }
    }
}

@Composable
private fun TopicDetailsUserInfoShimmer() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmer(shape = CircleShape),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.22f)
                    .height(18.dp)
                    .shimmer(shape = AppTheme.shapes.small),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.38f)
                    .height(14.dp)
                    .shimmer(shape = AppTheme.shapes.small),
            )
        }
    }
}

@Composable
private fun TopicDetailsTitleShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .height(28.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(28.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
    }
}

@Composable
private fun TopicDetailsBodyShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(18.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .height(18.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(18.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
    }
}

@Composable
private fun TopicDetailsQuoteShimmer() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(108.dp)
                .shimmer(shape = AppTheme.shapes.small),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(18.dp)
                    .shimmer(shape = AppTheme.shapes.small),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .height(18.dp)
                    .shimmer(shape = AppTheme.shapes.small),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(18.dp)
                    .shimmer(shape = AppTheme.shapes.small),
            )
        }
    }
}

@Composable
private fun TopicDetailsMediaShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(196.dp)
            .shimmer(shape = AppTheme.shapes.medium),
    )
}
