package com.dezdeqness.ui

import android.view.View
import com.dezdeqness.presentation.models.MessageEvent
import com.google.android.material.snackbar.Snackbar

object ShikimoriSnackbar {

    fun showSnackbarShort(
        view: View,
        anchorView: View,
        event: MessageEvent,
    ) =
        Snackbar
            .make(view, event.text, Snackbar.LENGTH_SHORT)
            .setTextColor(event.textColor)
            .setBackgroundTint(event.backgroundColor)
            .setAnchorView(anchorView)
            .show()

}
