package com.dezdeqness.presentation.features.animedetails

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.databinding.FragmentAnimeDetailsBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ArgsModule
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
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


class AnimeDetailsFragment : BaseFragment<FragmentAnimeDetailsBinding>(), ActionListener {

    @Inject
    lateinit var applicationRouter: ApplicationRouter

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter: AnimeDetailsAdapter by lazy {
        AnimeDetailsAdapter(
            actionListener = this,
            onStatsClicked = {
                viewModel.onStatsClicked()
            },
            onChronologyClicked = {
                viewModel.onChronologyClicked()
            },
            onSimilarClicked = {
                viewModel.onSimilarClicked()
            },
            onScreenShotClicked = { url ->
                viewModel.onScreenShotClicked(url)
            }
        )
    }

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

    private var title = ""

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeDetailsBinding.inflate(layoutInflater)

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

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedCallback?.handleOnBackPressed()
        }

        binding.editRate.setOnClickListener {
            viewModel.onEditRateClicked()
        }

        setupRecyclerView()
        setupObservers()
        setupScrollListener()
        setupMenu()
    }

    private fun setupMenu() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_anime)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        viewModel.onShareButtonClicked()
                    }
                }
                true
            }
            menu?.findItem(R.id.action_share)?.isVisible = false
        }
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
    }

    private fun setupScrollListener() {
        val titleFrame = Rect()
        val recyclerFrame = Rect()
        var targetView: View? = null

        binding.content.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (targetView == null) {
                    targetView = getTargetView() ?: return
                }

                targetView?.getGlobalVisibleRect(titleFrame)
                recyclerView.getGlobalVisibleRect(recyclerFrame)

                if (recyclerFrame.contains(titleFrame) && targetView?.isShown == true) {
                    viewModel.onToolbarVisibilityOff()
                } else {
                    viewModel.onToolbarVisibilityOn()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.content.adapter = adapter
        binding.content.errorLayout.findViewById<View>(R.id.retry)?.setOnClickListener {
            viewModel.onRetryButtonClicked()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeDetailsStateFlow.collect { state ->
                    title = state.title

                    setupLoadingState(state = state)
                    setupList(state = state)
                    setupShareButton(state = state)
                    setupToolbarVisibility(state = state)
                    setupEditRateFabVisibility(state = state)
                    setupErrorStateVisibility(state = state)
                }
            }
        }
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

    private fun setupLoadingState(state: AnimeDetailsState) {
        binding.content.setLoadingState(
            isLoadingStateShowing = state.isInitialLoadingIndicatorShowing,
        )
    }

    private fun setupErrorStateVisibility(state: AnimeDetailsState) {
        binding.content.setErrorState(
            isErrorStateShowing = state.isErrorStateShowing,
        )
    }

    private fun setupList(state: AnimeDetailsState) {
        if (state.uiModels.isNotEmpty()) {
            adapter.submitList(state.uiModels)
        }
    }

    private fun setupShareButton(state: AnimeDetailsState) {
        if (state.uiModels.isNotEmpty()) {
            binding.toolbar.menu?.findItem(R.id.action_share)?.let { menuItem ->
                if (menuItem.isVisible.not()) {
                    menuItem.isVisible = true
                }
            }
        }
    }

    private fun setupToolbarVisibility(state: AnimeDetailsState) {
        if (state.isToolbarVisible) {
            binding.toolbar.setBackgroundResource(R.color.details_toolbar_color_expand)
            binding.toolbar.title = title
        } else {
            binding.toolbar.setBackgroundResource(android.R.color.transparent)
            binding.toolbar.title = ""
        }
    }

    private fun getTargetView(): View? {
        binding.content.recyclerView.apply {
            if (this.childCount == 0 || getChildAt(0) !is ViewGroup) return null
            return (getChildAt(0) as ViewGroup).findViewWithTag("tag")
        }
    }

    private fun setupEditRateFabVisibility(state: AnimeDetailsState) {
        if (state.isEditRateFabShown) {
            binding.editRate.show()
        } else {
            binding.editRate.hide()
        }
    }

}
