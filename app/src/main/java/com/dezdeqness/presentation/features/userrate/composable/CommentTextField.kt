package com.dezdeqness.presentation.features.userrate.composable


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    state: CommentState,
    textStyle: TextStyle = AppTheme.typography.titleMedium.copy(
        fontSize = 14.sp,
        color = AppTheme.colors.textPrimary,
    ),
    onCommentChanged: (String) -> Unit,
    containerColor: Color = colorResource(id = R.color.search_container),
    shape: Shape = RoundedCornerShape(16.dp),
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done,
        capitalization = KeyboardCapitalization.Words,
    ),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
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

    OutlinedTextField(
        value = state.comment,
        onValueChange = {
            state.comment = it
            onCommentChanged(it)
        },
        placeholder = if (editTextFocused) {
            placeholder
        } else {
            null
        },
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, shape)
            .focusRequester(focusRequester)
            .onFocusChanged {
                editTextFocused = it.isFocused
            },
        textStyle = textStyle,
        singleLine = true,
        trailingIcon = if (state.comment.isNotBlank()) {
            trailingIcon
        } else {
            null
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = colors,
        minLines = 3,
    )
}

class CommentState {
    var comment by mutableStateOf("")

    companion object {
        val Saver: Saver<CommentState, *> = listSaver(
            save = { listOf(it.comment) },
            restore = {
                CommentState().apply {
                    comment = it[0]
                }
            }
        )
    }

}

@Composable
fun rememberCommentState(value: String): CommentState {
    val commentState = rememberSaveable(saver = CommentState.Saver, key = value) {
        CommentState().apply {
            comment = value
        }
    }

    if (value != commentState.comment) {
        commentState.comment = value
    }
    return commentState
}
