package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingPhase
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingStep
import com.dezdeqness.feature.onboarding.flow.presentation.phase
import com.dezdeqness.feature.onboarding.flow.presentation.wizardProgress
import com.dezdeqness.feature.onboarding.flow.presentation.wizardSteps
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingHeader(
    step: OnboardingStep,
    onBack: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDone = step == OnboardingStep.DONE
    AppToolbar(
        modifier = modifier,
        navigationIcon = if (isDone) Icons.Filled.Close else Icons.AutoMirrored.Filled.ArrowBack,
        navigationClick = if (isDone) onClose else onBack,
        content = {
            if (step.phase == OnboardingPhase.WIZARD) {
                OnboardingProgressBar(
                    totalSteps = wizardSteps.size,
                    currentStep = step.wizardProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                )
            }
        },
    )
}
