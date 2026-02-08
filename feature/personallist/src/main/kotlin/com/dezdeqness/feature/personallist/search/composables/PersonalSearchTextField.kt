package com.dezdeqness.feature.personallist.search.composables

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.personallist.R
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PersonalSearchTextField(
    modifier: Modifier,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit
) {
    val scope = rememberCoroutineScope()

    SearchBarDefaults.InputField(
        modifier = modifier,
        searchBarState = searchBarState,
        textFieldState = textFieldState,
        textStyle = AppTheme.typography.titleMedium.copy(
            color = AppTheme.colors.textPrimary,
        ),
        onSearch = {
            onSearch("")
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.personal_list_search_placeholder_hint),
                style = AppTheme.typography.titleMedium.copy(
                    color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                ),
            )
        },
        leadingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {

                IconButton(
                    onClick = {
                        scope.launch { searchBarState.animateToCollapsed() }
                        onSearch("")
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface
                    )
                }
            } else {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface
                )
            }
        },
        trailingIcon = {
            if (textFieldState.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd("")
                    },
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface
                    )
                }
            }
        },
    )
}
