package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            title,
            style = AppTheme.typography.labelLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(vertical = 16.dp),
            color = AppTheme.colors.textPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    AppTheme {
        Box(
            modifier = Modifier.width(200.dp)
        ) {
            SectionHeader(
                title = "Sci-Fi",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
