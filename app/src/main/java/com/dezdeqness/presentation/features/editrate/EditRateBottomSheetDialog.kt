package com.dezdeqness.presentation.features.editrate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.databinding.BottomSheetDialogEditRateBinding
import com.dezdeqness.di.subcomponents.UserRateModule
import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.features.sortdialog.SortBottomSheetDialog
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
    }

    private fun setupListeners() {
        binding.ratingView.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.onRatingChanged(rating.toLong() * 2)
            }
        }
        binding.rateEpisodeMinus.setOnClickListener {
            viewModel.onEpisodesMinusClicked()
        }
        binding.rateEpisodePlus.setOnClickListener {
            viewModel.onEpisodesPlusClicked()
        }
        binding.rateResetChanges.setOnClickListener {
            viewModel.onResetButtonClicked()
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
        binding.rateEdit.setOnClickListener {
            viewModel.onApplyButtonClicked()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editRateStateFlow.collect { state ->
                    binding.toolbar.title = state.title
                    binding.ratingView.rating = state.score / 2f
                    binding.rateWatches.text = state.episode.toString()
                    binding.currentStatus.text = state.status.status
                    binding.rateEdit.isEnabled = state.isUserRateChanged


                    state.events.forEach { event ->
                        when (event) {
                            is Event.EditUserRate -> {
                                setFragmentResult(
                                    requestKey = TAG,
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

    companion object {

        const val RESULT = "EditRateBottomSheetDialogResult"
        const val TAG = "EditRateBottomSheetDialog"
        private const val RATE_ID = "rate_id"

        fun newInstance(rateId: Long) =
            EditRateBottomSheetDialog().apply {
                arguments = bundleOf(
                    RATE_ID to rateId
                )
            }
    }

}
