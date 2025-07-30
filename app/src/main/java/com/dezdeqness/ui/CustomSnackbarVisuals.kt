package com.dezdeqness.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.dezdeqness.presentation.models.MessageEvent.MessageEventStatus

data class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val messageStatus: MessageEventStatus,
) : SnackbarVisuals