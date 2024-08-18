package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.SearchTextField
import com.dezdeqness.core.ui.rememberSearchState
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AnimeSearch(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
) {
    val searchState = rememberSearchState()

    SearchTextField(
        modifier = modifier,
        state = searchState,
        onQueryChanged = onQueryChanged,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_placeholder_hint),
                style = AppTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                ),
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    searchState.query = ""
                    onQueryChanged(searchState.query)
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = AppTheme.colors.onSecondary
                )
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = AppTheme.colors.onSecondary
            )
        }
    )

}
