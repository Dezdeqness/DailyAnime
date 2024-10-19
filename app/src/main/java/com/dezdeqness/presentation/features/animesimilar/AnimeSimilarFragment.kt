package com.dezdeqness.presentation.features.animesimilar

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dezdeqness.R
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.animesimilar.recyclerview.AnimeSimilarAdapter
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment

class AnimeSimilarFragment : GenericListableFragment() {

    private val args by navArgs<AnimeSimilarFragmentArgs>()

    override fun getTitleRes() = R.string.anime_similar_title

    override fun delegateAdapter() = AnimeSimilarAdapter(actionListener = this)

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeSimilarComponent()
            .argsModule(SimilarArgsModule(args.animeId))
            .build()
            .inject(this)
    }

    override fun onEvent(event: Event) =
        when (event) {
            is AnimeDetails -> {
                findNavController().navigate(
                    AnimeSimilarFragmentDirections.animeDetailsFragment(event.animeId)
                )
                true
            }

            else -> {
                false
            }
        }

}
