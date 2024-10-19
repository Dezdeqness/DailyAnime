package com.dezdeqness.presentation.event

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.dezdeqness.data.BuildConfig


class EventConsumer(
    val fragment: Fragment? = null,
    val context: Context,
) {

    fun consume(event: ConsumableEvent) {
        when (event) {
            is OpenVideo -> {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(event.url)
                    )
                )
            }
            is ShareUrl -> {
                val url = if (event.url.startsWith(BuildConfig.BASE_URL).not()) {
                    BuildConfig.BASE_URL + event.url
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
