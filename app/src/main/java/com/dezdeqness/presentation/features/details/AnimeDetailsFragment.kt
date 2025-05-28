package com.dezdeqness.presentation.features.details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.core.BaseComposeFragment
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToAnimeStats
import com.dezdeqness.presentation.event.NavigateToCharacterDetails
import com.dezdeqness.presentation.event.NavigateToChronology
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.NavigateToScreenshotViewer
import com.dezdeqness.presentation.event.NavigateToSimilar
import com.dezdeqness.presentation.features.userrate.UserRateActivity
import com.dezdeqness.presentation.routing.ApplicationRouter
import kotlinx.coroutines.launch
import javax.inject.Inject


class AnimeDetailsFragment : BaseComposeFragment() {

    @Inject
    lateinit var applicationRouter: ApplicationRouter

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
        )
    }

    private val viewModel: AnimeDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory },
    )

    private val editRateResult =
        registerForActivityResult(UserRateActivity.UserRate()) { userRate ->
            viewModel.onUserRateChanged(userRate)
        }

    override fun setupScreenComponent(component: AppComponent) {
        component
            .animeDetailsComponent()
            .argsModule(module = ArgsModule(parseTarget()))
            .build()
            .inject(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun FragmentContent() {
        AppTheme {
            DetailsPage(
                stateFlow = viewModel.animeDetailsStateFlow,
                actions = object : DetailsActions {
                    override fun onBackPressed() {
                        findNavController().navigateUp()
                    }

                    override fun onSharePressed() {
                        viewModel.onShareButtonClicked()
                    }

                    override fun onFabClicked() {
                        viewModel.onEditRateClicked()
                    }

                    override fun onActionReceive(action: Action) {
                        viewModel.onActionReceive(action)
                    }

                    override fun onRetryClicked() {
                        viewModel.onRetryButtonClicked()
                    }
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    onEvent(event)
                }
            }
        }
    }

    private fun onEvent(event: Event) {
        when (event) {
            is NavigateToEditRate -> {
                editRateResult.launch(
                    UserRateActivity.UserRateParams(
                        userRateId = event.rateId,
                        title = event.title,
                        overallEpisodes = event.overallEpisodes,
                    ),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity()),
                )
            }

            is NavigateToAnimeStats -> {
                val action = AnimeDetailsFragmentDirections.navigateToAnimeStatsAction(
                    event.scoreList.toTypedArray(),
                    event.statusesList.toTypedArray(),
                )
                findNavController().navigate(action)
            }

            is NavigateToSimilar -> {
                val action =
                    AnimeDetailsFragmentDirections.navigateToAnimeSimilarAction(event.animeId)
                findNavController().navigate(action)
            }

            is NavigateToChronology -> {
                val action =
                    AnimeDetailsFragmentDirections.navigateToAnimeChronologyAction(event.animeId)
                findNavController().navigate(action)
            }

            is NavigateToScreenshotViewer -> {
                applicationRouter.navigateToScreenshotViewerScreen(
                    context = requireContext(),
                    screenshots = event.screenshots,
                    index = event.currentIndex,
                )
            }

            is AnimeDetails -> {
                analyticsManager.detailsTracked(id = event.animeId.toString(), title = event.title)
                findNavController().navigate(
                    AnimeDetailsFragmentDirections.navigateToAnimeDetails(event.animeId)
                )
            }

            is NavigateToCharacterDetails -> {
                findNavController().navigate(
                    AnimeDetailsFragmentDirections.navigateToCharacter(event.characterId, 0)
                )
            }

            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }
    }

    private fun parseTarget(): Target {
        val arguments = requireArguments()

        val animeId = arguments.getLong("animeId")
        val characterId = arguments.getLong("characterId")

        return if (characterId != 0L) {
            Target.Character(characterId)
        } else {
            Target.Anime(animeId)
        }
    }

}
