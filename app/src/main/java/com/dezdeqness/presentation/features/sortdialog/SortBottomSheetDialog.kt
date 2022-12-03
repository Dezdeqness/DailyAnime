package com.dezdeqness.presentation.features.sortdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.dezdeqness.databinding.BottomSheetDialogSortBinding
import com.dezdeqness.domain.model.Sort
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogSortBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sort = requireArguments().getString(CURRENT_SORT)

        val sorts = Sort.values().sortedBy { it.sort }
        val index = sorts.indexOfFirst {
            it.sort == sort
        }

        binding.list.menu.apply {

            binding.list.setNavigationItemSelectedListener { menu ->
                setFragmentResult(
                    requestKey = TAG,
                    result = bundleOf(
                        RESULT to sorts[menu.itemId].sort,
                    ),
                )
                dismiss()
                true
            }
            sorts.forEachIndexed { index, sort -> add(0, index, index, sort.sort) }
            setGroupCheckable(0, true, true)
            binding.list.setCheckedItem(index)
        }

    }

    companion object {

        const val RESULT = "SortBottomSheetDialogResult"
        const val TAG = "SortBottomSheetDialog"
        private const val CURRENT_SORT = "current_sort"

        fun newInstance(sort: String) =
            SortBottomSheetDialog().apply {
                arguments = bundleOf(
                    CURRENT_SORT to sort
                )
            }

    }

}
