package com.dezdeqness.presentation.features.debugscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.data.core.config.ConfigKeys
import com.dezdeqness.presentation.features.settings.composables.SwitchSettingsView
import com.dezdeqness.presentation.features.settings.composables.TextSettingsView
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreenPage(
    modifier: Modifier = Modifier,
    uiState: StateFlow<DebugConfigState>,
    actions: DebugScreenActions,
) {
    LaunchedEffect(Unit) {
        actions.onInitialLoading()
    }

    val state by uiState.collectAsState()
    var editingKey by remember { mutableStateOf<ConfigKeys?>(null) }
    var inputValue by remember { mutableStateOf("") }

    val entries = state.configValues.entries.toList()

    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.onPrimary,
        topBar = {
            AppToolbar(
                title = "Debug",
                navigationClick = actions::onBackPressed,
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn {
                item {
                    SwitchSettingsView(
                        title = "Override remote variables",
                        checked = state.isOverrideEnabled,
                        onCheckedChanged = actions::onOverrideConfigKeysClicked
                    )
                }

                items(entries.size) { key ->
                    val item = entries[key]

                    when (val value = item.value) {
                        is Boolean -> {
                            SwitchSettingsView(
                                title = item.key.key,
                                checked = value,
                                enabled = state.isOverrideEnabled,
                                onCheckedChanged = { checked ->
                                    actions.setValue(item.key, checked)
                                }
                            )
                        }

                        else -> {
                            TextSettingsView(
                                title = item.key.key,
                                enabled = state.isOverrideEnabled,
                                subtitle = value.toString(),
                                onClick = {
                                    editingKey = item.key
                                    inputValue = value.toString()
                                }
                            )
                        }
                    }
                }

                item {
                    Box(modifier = Modifier.padding(80.dp))
                }
            }

            AnimatedVisibility(
                visible = state.isModified,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            ) {
                AppButton(
                    title = "Restart app to apply changes",
                    onClick = actions::onApplyChangesClicked,
                )
            }
        }
    }

    if (editingKey != null) {
        AlertDialog(
            onDismissRequest = { editingKey = null },
            title = { Text("Edit ${editingKey!!.key}") },
            text = {
                TextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val key = editingKey!!
                    val parsed = when (val default = key.defaultValue) {
                        is Int -> inputValue.toIntOrNull() ?: default
                        is String -> inputValue
                        else -> default
                    }
                    actions.setValue(key, parsed)
                    editingKey = null
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingKey = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
