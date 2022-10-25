package com.dezdeqness.presentation.features.animedetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeDetailsBinding
import com.dezdeqness.di.subcomponents.ArgsModule
import com.dezdeqness.getComponent
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs


class AnimeDetailsFragment : BaseFragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private val adapter: AnimeDetailsAdapter by lazy {
        AnimeDetailsAdapter()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AnimeDetailsViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    private var title = "Аниме"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity()
            .application
            .getComponent()
            .animeDetailsComponent()
            .argsModule(module = ArgsModule(requireArguments().getLong("animeId")))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
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

        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                }
            }
        }
    }

    abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
        enum class State {
            EXPANDED, COLLAPSED, IDLE
        }

        private var mCurrentState =
            State.IDLE

        override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
            mCurrentState = if (abs(i) >= appBarLayout.totalScrollRange * 0.99) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                State.COLLAPSED
            } else {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                State.EXPANDED
            }
        }

        abstract fun onStateChanged(
            appBarLayout: AppBarLayout?,
            state: State?
        )
    }
}
