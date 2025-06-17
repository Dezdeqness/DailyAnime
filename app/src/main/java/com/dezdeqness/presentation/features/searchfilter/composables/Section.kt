package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.ExpandableContent
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.presentation.models.SearchSectionUiModel
import com.dezdeqness.presentation.models.SectionType
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Section(
    modifier: Modifier = Modifier,
    sectionData: StateFlow<SearchSectionUiModel>,
    onScrollNeed: () -> Unit,
    onClick: (String, String, Boolean) -> Unit,
) {
    val state by sectionData.collectAsState()

    var isExpanded by rememberSaveable(state.innerId) {
        mutableStateOf(false)
    }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    val selectedCounter = state.selectedCells.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = AppTheme.colors.ripple),
                    onClick = {
                        isExpanded = isExpanded.not()
                    },
                    enabled = state.isExpandable,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Header(title = state.displayName)

            if (state.isExpandable) {
                Spacer(modifier = Modifier.weight(1f))

                if (selectedCounter > 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(AppTheme.colors.accent, CircleShape)
                        )

                        Text(
                            selectedCounter.toString(),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }

                }

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                        .graphicsLayer(rotationZ = rotationAngle),
                    tint = AppTheme.colors.textPrimary,
                )
            }
        }

        val isVisible = !state.isExpandable || isExpanded

        ExpandableContent(isVisible = isVisible) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                if (state.sectionType == SectionType.CheckBox) {
                    SearchFilterCheckboxGroup(
                        section = state,
                        onClick = { innerId, cellId, isSelected ->
                            onClick(
                                innerId,
                                cellId,
                                isSelected,
                            )
                        },
                        onScrollNeed = onScrollNeed,
                    )
                } else {
                    SearchFilterChipGroup(
                        section = state,
                        onClick = { innerId, cellId, isSelected ->
                            onClick(
                                innerId,
                                cellId,
                                isSelected,
                            )
                        }
                    )
                }
            }
        }
    }
}
