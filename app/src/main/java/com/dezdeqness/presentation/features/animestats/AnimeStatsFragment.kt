package com.dezdeqness.presentation.features.animestats

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.AnimeStatsArgsModule

class AnimeStatsFragment : BaseComposeFragment() {

    private val args by navArgs<AnimeStatsFragmentArgs>()

    private val viewModel: AnimeStatsViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeStatsComponent()
            .argsModule(
                AnimeStatsArgsModule(
                    AnimeStatsArguments(
                        scoresArgument = args.animeScores.toList(),
                        statusesArgument = args.animeStatuses.toList(),
                    )
                )
            )
            .build()
            .inject(this)
    }

    @Composable
    override fun FragmentContent() {

    }

}
