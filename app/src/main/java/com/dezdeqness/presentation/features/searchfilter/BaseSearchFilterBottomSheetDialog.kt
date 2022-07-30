package com.dezdeqness.presentation.features.searchfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.dezdeqness.databinding.BottomSheetDialogFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseSearchFilterBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogFilterBinding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogLayout()
        setupToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireDialog().cancel()
        }
    }

    private fun setupDialogLayout() {
        with(requireDialog().findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)) {
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            with(BottomSheetBehavior.from(this)) {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
    }

}
