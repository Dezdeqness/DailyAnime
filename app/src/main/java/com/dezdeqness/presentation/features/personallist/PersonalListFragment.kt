package com.dezdeqness.presentation.features.personallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalListBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.features.editrate.EditRateBottomSheetDialog
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.dezdeqness.presentation.features.sortdialog.SortBottomSheetDialog
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import kotlinx.coroutines.launch

class PersonalListFragment : BaseFragment<FragmentPersonalListBinding>() {

    private var ribbonState: List<RibbonStatusUiModel> = listOf()

    private val adapter: PersonalListAdapter by lazy {
        PersonalListAdapter(
            onAnimeClickListener = { animeId ->
                findNavController().navigate(
                    R.id.animeDetailsFragment,
                    bundleOf("animeId" to animeId),
                )
            },
            onSortClicked = {
                viewModel.onSortClicked()
            },
            onOrderClicked = { order ->
                viewModel.onOrderChanged(order)
            },
            onEditRateClicked = { editRate ->
                viewModel.onEditRateClicked(editRate)
            }
        )
    }

    private val viewModel: PersonalListViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun getFragmentBinding(layoutInflater: LayoutInflater) =
        FragmentPersonalListBinding.inflate(layoutInflater)

    override fun setupScreenComponent(component: AppComponent) =
        component
            .personalListComponent()
            .create()
            .inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(SortBottomSheetDialog.TAG) { _, bundle ->
            val sort = bundle.getString((SortBottomSheetDialog.RESULT))
            viewModel.onSortChanged(sort)
        }

        setFragmentResultListener(EditRateBottomSheetDialog.TAG) { _, bundle ->
            val userRate = bundle.getParcelable<EditRateUiModel>(EditRateBottomSheetDialog.RESULT)
            viewModel.onUserRateChanged(userRate)
        }

        viewModel.loadPersonalList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupSearchView()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(SortBottomSheetDialog.TAG)
        clearFragmentResultListener(EditRateBottomSheetDialog.TAG)
    }

    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onQueryChanged(newText)
                return true

            }

        })
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onRefreshSwiped()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalListStateFlow.collect { state ->
                    binding.refresh.isRefreshing = state.isPullDownRefreshing
                    if (ribbonState.isEmpty() || ribbonState != state.ribbon) {
                        ribbonState = state.ribbon
                        binding.ribbon.populate(
                            list = ribbonState,
                            listener = { id ->
                                viewModel.onRibbonItemSelected(id = id)
                            }
                        )
                    }
                    adapter.submitList(state.items) {
                        if (state.events.contains(Event.ScrollToTop)) {
                            binding.personalList.scrollToPosition(0)
                            viewModel.onEventConsumed(Event.ScrollToTop)
                        }
                    }

                    state.events.forEach { event ->
                        when (event) {
                            is Event.NavigateToSortFilter -> {
                                val dialog =
                                    SortBottomSheetDialog.newInstance(event.currentSort)
                                dialog.show(
                                    parentFragmentManager,
                                    SortBottomSheetDialog.TAG
                                )
                            }

                            is Event.NavigateToEditRate -> {
                                val dialog =
                                    EditRateBottomSheetDialog.newInstance(event.rateId)
                                dialog.show(
                                    parentFragmentManager,
                                    EditRateBottomSheetDialog.TAG
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

    private fun setupRecyclerView() {
        binding.personalList.adapter = adapter
    }

}
