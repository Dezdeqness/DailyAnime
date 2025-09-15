package com.dezdeqness.presentation.features.personallist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.textfield.SearchTextField
import com.dezdeqness.core.ui.views.textfield.rememberSearchState

@Composable
fun PersonalListSearch(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
) {
    val searchState = rememberSearchState()

    LaunchedEffect(searchState.query) {
        if (searchState.hasUserInteracted) {
            onQueryChanged(searchState.query)
        }
    }

    SearchTextField(
        modifier = modifier,
        state = searchState,
        onQueryChanged = onQueryChanged,
        placeholder = {
            Text(
                text = stringResource(id = R.string.personal_list_search_placeholder_hint),
                style = AppTheme.typography.titleMedium.copy(
                    color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                ),
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    searchState.updateQuery("")
                    onQueryChanged(searchState.query)
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface
                )
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = AppTheme.colors.onSurface
            )
        },
    )

}

@Preview
@Composable
fun PersonaListSearchPreview() {
    AppTheme {
        Box(
            modifier = Modifier.background(AppTheme.colors.onPrimary)
        ) {
            PersonalListSearch(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onQueryChanged = {},
            )
        }
    }
}
