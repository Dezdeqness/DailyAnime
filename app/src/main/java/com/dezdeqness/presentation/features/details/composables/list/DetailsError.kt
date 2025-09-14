package com.dezdeqness.presentation.features.details.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton

@Composable
fun DetailsError(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val compositionLoading by rememberLottieComposition(
                LottieCompositionSpec.RawRes(com.dezdeqness.core.ui.R.raw.error)
            )

            LottieAnimation(
                composition = compositionLoading,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.height(120.dp)
            )

            Text(
                text = stringResource(id = com.dezdeqness.core.ui.R.string.error_state_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary,
            )
        }

        AppButton(
            title = stringResource(id = com.dezdeqness.R.string.anime_details_state_error_title),
            onClick = onRetryClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).align(Alignment.BottomCenter)
        )
    }
}

@PreviewLightDark
@Composable
fun DetailsErrorPreview() {
    AppTheme {
        DetailsError(onRetryClick = {})
    }
}
