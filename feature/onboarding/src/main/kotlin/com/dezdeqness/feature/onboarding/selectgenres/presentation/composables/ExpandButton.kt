package com.dezdeqness.feature.onboarding.selectgenres.presentation.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.onboarding.R

@Composable
fun ExpandButton(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedContent(
            targetState = expanded,
            label = "expand collapse text",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { isExpanded ->
            Text(
                text = stringResource(
                    if (isExpanded) {
                        R.string.onboarding_select_genres_collapse
                    } else {
                        R.string.onboarding_select_genres_expand
                    },
                ),
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.labelLarge,
            )
        }

        Spacer(Modifier.width(4.dp))

        val rotation by animateFloatAsState(
            targetValue = if (expanded) 270f else 90f,
            label = "arrow rotation"
        )

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = AppTheme.colors.textPrimary,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer { rotationZ = rotation }
        )
    }
}
