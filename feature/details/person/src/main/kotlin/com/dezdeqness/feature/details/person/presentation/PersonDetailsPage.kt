package com.dezdeqness.feature.details.person.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.feature.details.common.presentation.DetailsBaseUiEvent
import com.dezdeqness.feature.details.common.presentation.DetailsContent
import com.dezdeqness.feature.details.common.presentation.DetailsPage
import com.dezdeqness.feature.details.common.presentation.composables.DetailsLoadingSkeleton
import com.dezdeqness.feature.details.common.presentation.utils.LocalDetailsRenderManagerComposition
import com.dezdeqness.feature.details.person.presentation.preview.PersonDetailsPreviewData
import com.dezdeqness.feature.details.person.presentation.store.PersonDetailsNamespace
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PersonDetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<PersonDetailsNamespace.State>,
    onUiEvent: (PersonDetailsUiEvent) -> Unit,
    onBackPressed: () -> Unit,
) {
    val manager = remember { personDetailsRenderManager() }

    CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
        DetailsPage(
            modifier = modifier,
            stateFlow = stateFlow,
            onUiEvent = onUiEvent,
            onShare = { onUiEvent(PersonDetailsUiEvent.Base(DetailsBaseUiEvent.SharePressed)) },
            onRetry = { onUiEvent(PersonDetailsUiEvent.Base(DetailsBaseUiEvent.RetryClicked)) },
            onBack = onBackPressed,
            loading = { modifier ->
                DetailsLoadingSkeleton(modifier = modifier, descriptionLines = 3)
            },
        )
    }
}

@PreviewLightDark
@Composable
fun PersonDetailsContentPreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            val manager = remember { personDetailsRenderManager() }

            CompositionLocalProvider(LocalDetailsRenderManagerComposition provides manager) {
                DetailsContent<PersonDetailsUiEvent>(
                    sections = PersonDetailsPreviewData.sections,
                    onEvent = {},
                )
            }
        }
    }
}
