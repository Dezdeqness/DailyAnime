package com.dezdeqness.presentation.features.animechronology

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dezdeqness.R
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ChronologyArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.animechronology.composable.ChronologyItem
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableFragment
import com.dezdeqness.presentation.features.genericlistscreen.GenericRenderer
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ChronologyUiModel
import javax.inject.Inject

class AnimeChronologyFragment : GenericListableFragment() {

    private val args by navArgs<AnimeChronologyFragmentArgs>()

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun getTitleRes() = R.string.anime_chronology_title

    override val renderer: GenericRenderer
        get() = object : GenericRenderer {
            @Composable
            override fun Render(modifier: Modifier, item: AdapterItem, onClick: (Action) -> Unit) {
                if (item !is ChronologyUiModel) return

                ChronologyItem(
                    modifier = modifier,
                    item = item,
                    onClick = onClick,
                )
            }

        }

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
                analyticsManager.detailsTracked(id = event.animeId.toString(), title = event.title)
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
