package com.dezdeqness.presentation.features.animechronology

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dezdeqness.R
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.animechronology.recyclerview.AnimeChronologyAdapter
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment

class AnimeChronologyFragment : GenericListableFragment() {

    private val args by navArgs<AnimeChronologyFragmentArgs>()

    override fun getTitleRes() = R.string.anime_chronology_title

    override fun delegateAdapter() = AnimeChronologyAdapter(actionListener = this)

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeChronologyComponent()
            .argsModule(ChronologyArgsModule(args.animeId))
            .build()
            .inject(this)
    }

    override fun onEvent(event: Event) =
        when (event) {
            is AnimeDetails -> {
                findNavController().navigate(
                    AnimeChronologyFragmentDirections.animeDetailsFragment(event.animeId)
                )
                true
            }

            else -> {
                false
            }
        }

}
