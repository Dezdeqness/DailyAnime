package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AnimeSearchLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
fun AnimeSearchLoadingPreview() {
    AppTheme {
        AnimeSearchLoading(
            modifier = Modifier.background(AppTheme.colors.onPrimary).size(300.dp)
        )
    }
}
