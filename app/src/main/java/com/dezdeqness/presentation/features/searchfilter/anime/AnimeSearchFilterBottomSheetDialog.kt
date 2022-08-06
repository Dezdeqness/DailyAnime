package com.dezdeqness.presentation.features.searchfilter.anime

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.databinding.ItemAnimeSearchFilterBinding
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.features.searchfilter.BaseSearchFilterBottomSheetDialog
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
                    if (state.items.isNotEmpty()) {
                        binding.filterContainer.removeAllViews()
                        state.items.forEach { filter ->
                            val filterBinding =
                                ItemAnimeSearchFilterBinding.inflate(layoutInflater).apply {
                                    this.filter.setSearchChipListener(object :
                                        SearchFilterView.SearchChipListener {
                                        override fun onClickListener(itemId: String, cellId: String) {

                                        }

                                        override fun onLongClickListener(
                                            itemId: String,
                                            cellId: String
                                        ) {

                                        }
                                    })
                                    this.filter.setFilter(filter)
                                }
                            binding.filterContainer.addView(filterBinding.root)
                        }
                    }

                }
            }
        }
    }

    companion object {
        fun newInstance() = AnimeSearchFilterBottomSheetDialog().apply {
            arguments = bundleOf()
        }
    }
}
