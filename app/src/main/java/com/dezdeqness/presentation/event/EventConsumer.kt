package com.dezdeqness.presentation.event

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.data.BuildConfig


class EventConsumer(
    val fragment: Fragment? = null,
    val context: Context,
) {

    fun consume(event: ConsumableEvent) {
        when (event) {
            is AnimeDetails -> {
                fragment
                    ?.findNavController()
                    ?.navigate(
                        R.id.details,
                        bundleOf(
                            "animeId" to event.animeId
                        ),
                    )
            }
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
