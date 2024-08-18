package com.dezdeqness.presentation.features.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun CalendarDateHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        modifier = modifier
            .background(color = colorResource(id = R.color.background_tint))
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start,
        color = AppTheme.colors.textPrimary,
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarDateHeaderPreview() {
    AppTheme {
        CalendarDateHeader(
            title = "Sunday, August 18",
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}
