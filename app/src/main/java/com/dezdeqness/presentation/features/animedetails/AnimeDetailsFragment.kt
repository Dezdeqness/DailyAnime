package com.dezdeqness.presentation.features.animedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeDetailsBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.subcomponents.ArgsModule
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.features.editrate.EditRateBottomSheetDialog
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.launch
import kotlin.math.abs


class AnimeDetailsFragment : BaseFragment<FragmentAnimeDetailsBinding>() {

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter: AnimeDetailsAdapter by lazy {
        AnimeDetailsAdapter()
    }

    private val viewModel: AnimeDetailsViewModel by viewModels(factoryProducer = { viewModelFactory })

    private var title = "Аниме"

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentAnimeDetailsBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .animeDetailsComponent()
            .argsModule(module = ArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        setFragmentResultListener(EDIT_RATE_DIALOG_TAG) { _, bundle ->
            val userRate = bundle.getParcelable<EditRateUiModel>(EditRateBottomSheetDialog.RESULT)
            viewModel.onUserRateChanged(userRate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(
                appBarLayout: AppBarLayout?,
                state: State?
            ) {
                if (state == State.COLLAPSED) {
                    binding.toolbar.title = title
                    binding.toolbar.setBackgroundResource(android.R.color.white)

                } else if (state == State.EXPANDED) {
                    binding.toolbar.title = ""
                    binding.toolbar.setBackgroundResource(android.R.color.transparent)
                }
            }
        })

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedCallback?.handleOnBackPressed()
        }

        binding.animeChangeRate.setOnClickListener {
            viewModel.onEditRateClicked()
        }

        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(EDIT_RATE_DIALOG_TAG)
    }

    private fun setupRecyclerView() {
        binding.content.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeDetailsStateFlow.collect { state ->
                    binding.nameView.text = state.title
                    title = state.title
                    binding.ratingValueView.text = state.ratingScore.toString()
                    binding.ratingView.rating = state.ratingScore / 2
                    binding.animeChangeRate.isVisible = state.isEditUserRateShowing

                    if (state.uiModels.isNotEmpty()) {
                        adapter.submitList(state.uiModels)
                    }

                    Glide
                        .with(requireContext())
                        .load(state.imageUrl)
                        .apply(
                            RequestOptions.bitmapTransform(
                                MultiTransformation(
                                    CenterCrop(),
                                    BlurTransformation(25)
                                ),
                            )
                        )
                        .into(binding.backgroundImage)

                    Glide
                        .with(requireContext())
                        .load(state.imageUrl)
                        .centerCrop()
                        .into(binding.imageView)
                    state.events.forEach { event ->
                        when (event) {
                            is NavigateToEditRate -> {
                                val dialog = EditRateBottomSheetDialog.newInstance(
                                    rateId = event.rateId,
                                    tag = EDIT_RATE_DIALOG_TAG,
                                )
                                dialog.show(
                                    parentFragmentManager,
                                    EDIT_RATE_DIALOG_TAG,
                                )
                            }

                            else -> {}
                        }

                        viewModel.onEventConsumed(event)
                    }
                }
            }
        }
    }

    abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
        enum class State {
            EXPANDED, COLLAPSED, IDLE
        }

        private var currentState = State.IDLE

        override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
            currentState = if (abs(i) >= appBarLayout.totalScrollRange * 0.99) {
                if (currentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                State.COLLAPSED
            } else {
                if (currentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                State.EXPANDED
            }
        }

        abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
    }

    companion object {
        private const val EDIT_RATE_DIALOG_TAG = "anime_details_edit_rate_dialog"
    }
}
