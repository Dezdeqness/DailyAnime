package com.dezdeqness.presentation.features.searchfilter.anime

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.databinding.ItemAnimeSearchFilterBinding
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.features.searchfilter.BaseSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.view.SearchFilterView
import kotlinx.coroutines.launch

class AnimeSearchFilterBottomSheetDialog : BaseSearchFilterBottomSheetDialog() {

    private val viewModel: AnimeSearchFilterViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity()
            .application
            .getComponent()
            .animeSearchFilterComponent()
            .create()
            .inject(this)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeSearchFilterStateFlow.collect { state ->
                    setInitialState(state)
                    updateCell(state)
                    viewModel.nullifyCurrentCell()
                }
            }
        }
    }

    private fun updateCell(state: AnimeSearchFilterState) {
        binding
            .filterContainer
            .children
            .filterIsInstance<SearchFilterView>()
            .forEach { searchFilterView ->
                if (searchFilterView.updateChip(state.currentCellUpdate)) {
                    return
                }
            }
    }

    private fun setInitialState(state: AnimeSearchFilterState) {
        if (state.items.isNotEmpty() && binding.filterContainer.childCount == 0) {
            state.items.forEach { filter ->
                val filterBinding = createSearchFilter(filter)
                binding.filterContainer.addView(filterBinding.root)
            }
        }
    }

    private fun createSearchFilter(animeFilter: AnimeSearchFilter): ItemAnimeSearchFilterBinding {
        val filterBinding =
            ItemAnimeSearchFilterBinding.inflate(layoutInflater).apply {
                filter.setFilterData(animeFilter)
                filter.setSearchChipListener(object :
                    SearchFilterView.SearchChipListener {
                    override fun onClickListener(item: AnimeCell) {
                        viewModel.onCellClicked(item)
                    }

                    override fun onLongClickListener(item: AnimeCell) {
                        viewModel.onCellLongClicked(item)
                    }
                })
            }
        return filterBinding
    }

    companion object {

        const val TAG = "AnimeSearchFilterBottomSheetDialog"

        fun newInstance() = AnimeSearchFilterBottomSheetDialog().apply {
            arguments = bundleOf()
        }
    }
}
