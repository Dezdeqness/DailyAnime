package com.dezdeqness.presentation.features.editrate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.ViewPager
import com.dezdeqness.R
import com.dezdeqness.databinding.BottomSheetDialogEditRateBinding
import com.dezdeqness.di.subcomponents.UserRateModule
import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.event.EditUserRate
import com.dezdeqness.presentation.features.editrate.CarouselPicker.CarouselItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


class EditRateBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: BottomSheetDialogEditRateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditRateViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    private var carouselAdapter: CarouselViewAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity()
            .application
            .getComponent()
            .editRateComponent()
            .userRateModule(UserRateModule(requireArguments().getLong(RATE_ID)))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogEditRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
        setupDialogLayout()
        setupScoreCarousel()
        setupMenu()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireDialog().cancel()
        }
    }
    private fun setupMenu() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_edit_rate)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_accept -> {
                        viewModel.onApplyButtonClicked()
                    }

                    R.id.action_reset -> {
                        viewModel.onResetButtonClicked()
                    }
                }
                true
            }
        }
    }

    private fun setupScoreCarousel() {
        binding.scoreCarousel.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageSelected(position: Int) {
                    viewModel.onRatingChanged(position)
                }

                override fun onPageScrollStateChanged(state: Int) {}
            }
        )
    }

    private fun setupListeners() {
        binding.rateEpisodeMinus.setOnClickListener {
            viewModel.onEpisodesMinusClicked()
        }
        binding.rateEpisodePlus.setOnClickListener {
            viewModel.onEpisodesPlusClicked()
        }
        binding.rateStatusPlanned.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.PLANNED)
        }
        binding.rateStatusHold.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.ON_HOLD)
        }
        binding.rateStatusWatched.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.COMPLETED)
        }
        binding.rateStatusRewatching.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.REWATCHING)
        }
        binding.rateStatusThrowed.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.DROPPED)
        }
        binding.rateStatusWatching.setOnClickListener {
            viewModel.onStatusChanged(UserRateStatusEntity.WATCHING)
        }
    }

    private fun setupDialogLayout() {
        with(requireDialog().findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)) {
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            with(BottomSheetBehavior.from(this)) {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editRateStateFlow.collect { state ->
                    binding.animeName.text = state.title
                    binding.rateWatches.text = state.episode.toString()
                    binding.currentStatus.text = state.status.displayName

                    setupAcceptButton(state = state)

                    if (carouselAdapter == null && state.carouselUiModels.isNotEmpty()) {
                        carouselAdapter = CarouselViewAdapter(
                            context = requireContext(),
                            items = state.carouselUiModels.map { CarouselItem(it.value.toString()) }
                        )
                        binding.scoreCarousel.adapter = carouselAdapter
                    }

                    binding.scoreCarousel.currentItem = state.score.toInt()

                    state.events.forEach { event ->
                        when (event) {
                            is EditUserRate -> {
                                setFragmentResult(
                                    requestKey = requireArguments().getString(TAG).orEmpty(),
                                    result = bundleOf(
                                        RESULT to event.userRateUiModel,
                                    ),
                                )
                                dismiss()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setupAcceptButton(state: EditRateState) {
        binding.toolbar.menu?.findItem(R.id.action_accept)?.let {
            it.isEnabled = state.isUserRateChanged
            it.icon?.alpha = if (state.isUserRateChanged) 255 else 100
        }
    }

    companion object {

        const val RESULT = "EditRateBottomSheetDialogResult"
        private const val RATE_ID = "rate_id"
        private const val TAG = "tag"

        fun newInstance(rateId: Long, tag: String) =
            EditRateBottomSheetDialog().apply {
                arguments = bundleOf(
                    RATE_ID to rateId,
                    TAG to tag,
                )
            }
    }

}
