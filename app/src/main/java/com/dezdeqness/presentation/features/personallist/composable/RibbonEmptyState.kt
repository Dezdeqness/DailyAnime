package com.dezdeqness.presentation.features.personallist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun RibbonEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.ribbon_empty_state_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary,
        )
        Text(
            text = stringResource(R.string.ribbon_empty_state_description),
            textAlign = TextAlign.Center,
            modifier = Modifier,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
        )


    }
}

@PreviewLightDark
@Composable
fun RibbonEmptyStatePreview() {
    AppTheme {
        RibbonEmptyState(
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .size(300.dp)
        )
    }
}
