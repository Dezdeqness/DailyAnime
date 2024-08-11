package com.dezdeqness.presentation.features.animelist.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun AnimeSearch(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var editTextFocused by remember {
        mutableStateOf(false)
    }

    BackHandler(enabled = editTextFocused) {
        if (editTextFocused) {
            focusManager.clearFocus()
        }
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = if (editTextFocused) {
            @Composable {
                Text(
                    text = stringResource(id = R.string.search_placeholder_hint),
                    style = AppTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                    ),
                )
            }
        } else {
            null
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.search_container),
                RoundedCornerShape(16.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged {
                editTextFocused = it.isFocused
            },
        textStyle = AppTheme.typography.titleMedium.copy(
            fontSize = 18.sp,
            color = AppTheme.colors.textPrimary,
        ),
        singleLine = true,
        trailingIcon = if (searchQuery.isNotBlank()) {
            @Composable {
                IconButton(
                    onClick = {
                        searchQuery = ""
                        onQueryChanged(searchQuery)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = AppTheme.colors.onSecondary
                    )
                }
            }
        } else {
            null
        },
        leadingIcon = if (editTextFocused.not() && searchQuery.isBlank()) {
            @Composable {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = AppTheme.colors.onSecondary
                )
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onQueryChanged(searchQuery)
        }),
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )

}
