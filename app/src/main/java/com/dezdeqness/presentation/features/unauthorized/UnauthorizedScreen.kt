package com.dezdeqness.presentation.features.unauthorized

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton
import com.dezdeqness.core.ui.views.buttons.AppOutlinedButton

@Composable
fun UnauthorizedScreen(
    title: String,
    actions: UnauthorizedActions,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.onPrimary)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = com.dezdeqness.core.ui.R.drawable.ic_placeholder),
            contentDescription = null,
        )

        Text(
            text = title,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppButton(
                title = stringResource(id = R.string.unauthorized_sign_in),
                onClick = {
                    actions.onSignInClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            AppOutlinedButton(
                title = stringResource(id = R.string.unauthorized_sign_up),
                onClick = {
                    actions.onSignUpClicked()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun PreviewUnauthorizedScreen() {
    AppTheme {
        UnauthorizedScreen(
            title = stringResource(id = R.string.unauthorized_title_profile),
            modifier = Modifier
                .height(600.dp)
                .width(400.dp),
            actions = object : UnauthorizedActions {
                override fun onSignInClicked() = Unit

                override fun onSignUpClicked() = Unit
            }
        )
    }
}
