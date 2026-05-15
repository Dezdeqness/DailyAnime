package com.dezdeqness.presentation.event

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.dezdeqness.ShikimoriApp


class EventConsumer(val context: Context) {

    private val baseUrl: String =
        (context.applicationContext as ShikimoriApp).appComponent
            .configManager.baseUrl.trimEnd('/')

    fun consume(event: ConsumableEvent) {
        when (event) {
            is OpenVideo -> {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        event.url.toUri()
                    )
                )
            }
            is ShareUrl -> {
                val url = if (event.url.startsWith(baseUrl).not()) {
                    baseUrl + event.url
                } else {
                    event.url
                }
                ShareCompat.IntentBuilder(context)
                    .setType("text/plain")
                    .setText(url)
                    .startChooser()
            }
        }
    }

}
