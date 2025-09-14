package com.dezdeqness.core.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dezdeqness.core.ui.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun GeneralError(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.error_state_title),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val compositionLoading by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.error)
        )

        LottieAnimation(
            composition = compositionLoading,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.height(120.dp)
        )

        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary,
        )
    }
}

@Preview
@Composable
fun GeneralErrorPreview() {
    AppTheme {
        GeneralError(
            modifier = Modifier
                .background(AppTheme.colors.onPrimary)
                .size(300.dp)
        )
    }
}
