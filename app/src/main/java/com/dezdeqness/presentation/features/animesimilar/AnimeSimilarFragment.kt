package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.R
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.SimilarArgsModule
import com.dezdeqness.presentation.features.animesimilar.recyclerview.AnimeSimilarAdapter
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment

class AnimeSimilarFragment : GenericListableFragment() {

    override fun getTitleRes() = R.string.anime_similar_title

    override fun delegateAdapter() = AnimeSimilarAdapter(actionListener = this)

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeSimilarComponent()
            .argsModule(SimilarArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
    }

}
