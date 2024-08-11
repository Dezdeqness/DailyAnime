package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AnimeSearchEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.search_empty_state_title),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary,
        )
    }
}

@Preview
@Composable
fun AnimeSearchEmptyPreview() {
    AppTheme {
        AnimeSearchEmpty(
            modifier = Modifier.background(AppTheme.colors.onPrimary).size(300.dp)
        )
    }
}
