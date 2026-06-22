package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingStep
import com.dezdeqness.feature.onboarding.flow.presentation.actionTitleRes
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.buttons.AppButton
import com.dezdeqness.foundation.ui.views.buttons.AppTextButton

@Composable
fun OnboardingBottomBar(
    step: OnboardingStep,
    genresValid: Boolean,
    onSkip: () -> Unit,
    onContinue: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp, bottom = 16.dp),
    ) {
        AnimatedVisibility(
            visible = step == OnboardingStep.WELCOME,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column {
                AppTextButton(
                    title = stringResource(R.string.onboarding_skip),
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        val onPrimaryClick = when (step) {
            OnboardingStep.DONE -> onFinish
            else -> onContinue
        }
        val enabled = step != OnboardingStep.GENRES || genresValid

        AppButton(
            onClick = onPrimaryClick,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(180)) },
                label = "onboarding_primary_button_label",
            ) { current ->
                Text(
                    text = stringResource(current.actionTitleRes),
                    style = AppTheme.typography.titleMedium,
                )
            }
        }
    }
}
