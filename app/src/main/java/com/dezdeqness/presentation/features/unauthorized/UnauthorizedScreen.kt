package com.dezdeqness.presentation.features.unauthorized

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppTextButton

@Composable
fun UnauthorizedScreen(
    actions: UnauthorizedActions,
    modifier: Modifier = Modifier,
) {
    val compositionLoading by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.no_face)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LottieAnimation(
                composition = compositionLoading,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.height(250.dp)
            )

            Text(
                text = stringResource(R.string.unauthorized_title_personal_list),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            AppTextButton(
                title = stringResource(id = R.string.unauthorized_navigation_profile),
                onClick = {
                    actions.onNavigateProfileClicked()
                },
            )


        }
    }
}

@Preview
@Composable
fun PreviewUnauthorizedScreen() {
    AppTheme {
        UnauthorizedScreen(
            modifier = Modifier
                .height(600.dp)
                .width(400.dp),
            actions = object : UnauthorizedActions {
                override fun onNavigateProfileClicked() = Unit
            }
        )
    }
}
