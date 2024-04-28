package com.dezdeqness.core_ui.views

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.dezdeqness.core_ui.theme.AppTheme

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    shape: Shape = AppTheme.shapes.medium,
    onClick: () -> Unit = {}
) {
    Button(
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
