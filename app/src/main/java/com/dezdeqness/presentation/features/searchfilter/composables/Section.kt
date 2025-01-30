package com.dezdeqness.presentation.features.searchfilter.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.ExpandableContent
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.SearchSectionUiModel
import com.dezdeqness.presentation.models.SectionType
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Section(
    sectionData: StateFlow<SearchSectionUiModel>,
    onClick: (String, String) -> Unit,
) {
    val state by sectionData.collectAsState()

    var isExpanded by rememberSaveable(state.innerId) {
        mutableStateOf(false)
    }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = isExpanded.not() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                state.displayName,
                style = AppTheme.typography.labelLarge.copy(fontSize = 20.sp),
                modifier = Modifier.padding(vertical = 16.dp),
                color = AppTheme.colors.textPrimary
            )

            if (state.isExpandable) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer(rotationZ = rotationAngle)
                )
            }
        }

        val isVisible = !state.isExpandable || isExpanded

        ExpandableContent(isVisible = isVisible) {
            Column(modifier = Modifier.wrapContentHeight()) {
                if (state.sectionType == SectionType.CheckBox) {
                    state.items.forEach {
                        Text(it.displayName)
                    }
                } else {
                    SearchFilterChipGroup(
                        section = state,
                        onClick = { cellId ->
                            onClick(
                                state.innerId,
                                cellId,
                            )
                        }
                    )
                }
            }
        }
    }
}
