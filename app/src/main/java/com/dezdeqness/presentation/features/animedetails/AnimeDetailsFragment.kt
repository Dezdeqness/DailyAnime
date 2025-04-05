package com.dezdeqness.presentation.features.animedetails

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
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
import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToAnimeState
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
            .argsModule(module = ArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
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

    private fun setupMenu() {
//        binding.toolbar.apply {
//            inflateMenu(R.menu.menu_anime)
//            setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.action_share -> {
//                        viewModel.onShareButtonClicked()
//                    }
//                }
//                true
//            }
//            menu?.findItem(R.id.action_share)?.isVisible = false
//        }
    }

//    private fun setupScrollListener() {
//        val titleFrame = Rect()
//        val recyclerFrame = Rect()
//        var targetView: View? = null
//
//        binding.content.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (targetView == null) {
//                    targetView = getTargetView() ?: return
//                }
//
//                targetView?.getGlobalVisibleRect(titleFrame)
//                recyclerView.getGlobalVisibleRect(recyclerFrame)
//
//                if (recyclerFrame.contains(titleFrame) && targetView?.isShown == true) {
//                    viewModel.onToolbarVisibilityOff()
//                } else {
//                    viewModel.onToolbarVisibilityOn()
//                }
//            }
//        })
//    }

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

            is NavigateToAnimeState -> {
                val action = AnimeDetailsFragmentDirections.navigateToAnimeStatsAction(
                    event.scoreList.toTypedArray(),
                    event.statusesList.toTypedArray(),
                )
                findNavController().navigate(action)
            }

            is NavigateToSimilar -> {
                val action = AnimeDetailsFragmentDirections.navigateToAnimeSimilarAction(event.animeId)
                findNavController().navigate(action)
            }

            is NavigateToChronology -> {
                val action = AnimeDetailsFragmentDirections.navigateToAnimeChronologyAction(event.animeId)
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

            is ConsumableEvent -> {
                eventConsumer.consume(event)
            }

            else -> {}
        }
    }

//    private fun setupToolbarVisibility(state: AnimeDetailsState) {
//        if (state.isToolbarVisible) {
//            binding.toolbar.setBackgroundResource(R.color.details_toolbar_color_expand)
//            binding.toolbar.title = title
//        } else {
//            binding.toolbar.setBackgroundResource(android.R.color.transparent)
//            binding.toolbar.title = ""
//        }
//    }
//
//    private fun getTargetView(): View? {
//        binding.content.recyclerView.apply {
//            if (this.childCount == 0 || getChildAt(0) !is ViewGroup) return null
//            return (getChildAt(0) as ViewGroup).findViewWithTag("tag")
//        }
//    }

}
