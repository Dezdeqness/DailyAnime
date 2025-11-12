package com.dezdeqness.core.ui.views.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    shape: Shape = AppTheme.shapes.medium,
    onClick: () -> Unit = {}
) {
    Button(
        shape = shape,
        enabled = enabled,
        modifier = Modifier
            .then(modifier),
        onClick = { onClick() },
    ) {
        Text(
            text = title,
            style = titleStyle,
        )
    }
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    shape: Shape = AppTheme.shapes.medium,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        shape = shape,
        modifier = Modifier
            .then(modifier),
        colors = ButtonDefaults.outlinedButtonColors().copy(containerColor = AppTheme.colors.onPrimary),
        onClick = { onClick() },
    ) {
        Text(
            text = title,
            style = titleStyle,
        )
    }
}

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    shape: Shape = AppTheme.shapes.medium,
    onClick: () -> Unit = {}
) {
    TextButton(
        shape = shape,
        modifier = Modifier
            .then(modifier),
        onClick = { onClick() },
    ) {
        Text(
            text = title,
            style = titleStyle,
        )
    }
}

sealed class ButtonColors() {
    // TODO: Fulfill with next tickets, by purpose
}

@PreviewLightDark
@Composable
fun AppButtonPreview() {
    AppTheme {
        AppButton(
            title = LoremIpsum(2).values.joinToString(),
            modifier = Modifier.background(AppTheme.colors.background).padding(16.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun AppOutlinedButtonPreview() {
    AppTheme {
        AppOutlinedButton(
            title = LoremIpsum(2).values.joinToString(),
            modifier = Modifier.background(AppTheme.colors.background).padding(16.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun AppTextButtonPreview() {
    AppTheme {
        AppTextButton(
            title = LoremIpsum(2).values.joinToString(),
            modifier = Modifier.background(AppTheme.colors.background).padding(16.dp)
        )
    }
}

