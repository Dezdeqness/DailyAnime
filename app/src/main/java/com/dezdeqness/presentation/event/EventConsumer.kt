package com.dezdeqness.presentation.event

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment

class EventConsumer(
    val fragment: BaseFragment<*>,
) {

    fun consume(event: ConsumableEvent) {
        when (event) {
            is AnimeDetails -> {
                fragment
                    .findNavController()
                    .navigate(
                        R.id.animeDetailsFragment,
                        bundleOf(
                            "animeId" to event.animeId
                        ),
                    )
            }
        }
    }

}
