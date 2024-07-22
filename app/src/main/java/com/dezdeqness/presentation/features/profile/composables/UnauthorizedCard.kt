package com.dezdeqness.presentation.features.profile.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.AppTextButton

@Composable
fun UnauthorizedCard(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().background(AppTheme.colors.onPrimary)
    ) {
        Text(
            stringResource(id = R.string.unauthorized_title_profile),
            style = AppTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(top = 8.dp).padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            AppTextButton(
                title = stringResource(id = R.string.unauthorized_sign_in),
                onClick = onLoginClick
            )

            AppTextButton(
                title = stringResource(id = R.string.unauthorized_sign_up),
                onClick = onRegisterClick
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UnauthorizedCardPreview() {
    AppTheme {
        UnauthorizedCard(
            onLoginClick = {},
            onRegisterClick = {},
        )
    }
}
