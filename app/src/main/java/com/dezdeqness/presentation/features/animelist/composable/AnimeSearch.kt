package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearch(
    modifier: Modifier = Modifier,
    scrollBehavior: SearchBarScrollBehavior,
    onQueryChanged: (String) -> Unit,
    historyContent: @Composable ColumnScope.(TextFieldState, SearchBarState) -> Unit,
) {
    val searchBarState = rememberSearchBarState()

    val textFieldState = rememberTextFieldState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchBarState.currentValue, textFieldState.text) {
        if (searchBarState.currentValue == SearchBarValue.Collapsed && textFieldState.text.isEmpty()) {
            onQueryChanged("")
        }
    }

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                textStyle = AppTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = AppTheme.colors.textPrimary,
                ),
                onSearch = {
                    onQueryChanged(textFieldState.text.toString())
                    scope.launch { searchBarState.animateToCollapsed() }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_placeholder_hint),
                        style = AppTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                        ),
                    )
                },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {

                        IconButton(
                            onClick = { scope.launch { searchBarState.animateToCollapsed() } }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                tint = AppTheme.colors.onSurface
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = AppTheme.colors.onSurface
                        )
                    }
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                if (searchBarState.currentValue == SearchBarValue.Collapsed) {
                                    onQueryChanged("")
                                }
                                textFieldState.setTextAndPlaceCursorAtEnd("")
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null,
                                tint = AppTheme.colors.onSurface
                            )
                        }
                    }
                },
            )
        }

    TopSearchBar(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        inputField = inputField,
        colors = SearchBarDefaults.colors(
            containerColor = AppTheme.colors.onPrimary,
        )
    )
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
    ) {
        historyContent(textFieldState, searchBarState)
    }
}
