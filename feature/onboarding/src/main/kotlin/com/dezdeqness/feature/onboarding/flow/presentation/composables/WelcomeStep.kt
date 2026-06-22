package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun WelcomeStep(
    modifier: Modifier = Modifier,
    animateOnLaunch: Boolean = true,
) {
    var visible by remember { mutableStateOf(!animateOnLaunch) }
    LaunchedEffect(Unit) { visible = true }

    val splashFontFamily = remember { FontFamily(Font(R.font.pacifico_regular)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        StaggeredAppear(visible = visible, delayMillis = 0) {
            Image(
                painter = painterResource(R.drawable.ic_onboarding_logo_foreground),
                contentDescription = null,
                modifier = Modifier.size(128.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAppear(visible = visible, delayMillis = 120) {
            Text(
                text = stringResource(R.string.onboarding_welcome_app_name),
                style = AppTheme.typography.displayMedium,
                fontFamily = splashFontFamily,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAppear(visible = visible, delayMillis = 240) {
            Text(
                text = stringResource(R.string.onboarding_welcome_subtitle),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
        }
    }
}

@Composable
private fun StaggeredAppear(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(durationMillis = 400, delayMillis = delayMillis)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 400, delayMillis = delayMillis),
                initialOffsetY = { it / 4 },
            ),
    ) {
        content()
    }
}
