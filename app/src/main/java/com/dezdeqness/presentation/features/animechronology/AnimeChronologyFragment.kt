package com.dezdeqness.presentation.features.animechronology

import com.dezdeqness.R
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.presentation.features.animechronology.recyclerview.AnimeChronologyAdapter
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment

class AnimeChronologyFragment : GenericListableFragment() {

    override fun getTitleRes() = R.string.anime_chronology_title

    override fun delegateAdapter() = AnimeChronologyAdapter(actionListener = this)

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeChronologyComponent()
            .argsModule(ChronologyArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
    }

}
