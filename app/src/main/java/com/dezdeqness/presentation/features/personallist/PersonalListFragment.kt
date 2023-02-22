package com.dezdeqness.presentation.features.personallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalListBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.NavigateToSortFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.features.editrate.EditRateBottomSheetDialog
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.dezdeqness.presentation.features.sortdialog.SortBottomSheetDialog
import kotlinx.coroutines.launch

class PersonalListFragment : BaseFragment<FragmentPersonalListBinding>(), ActionListener {

    private val adapter: PersonalListAdapter by lazy {
        PersonalListAdapter(
            actionListener = this,
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

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
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

        setFragmentResultListener(EDIT_RATE_DIALOG_TAG) { _, bundle ->
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
        clearFragmentResultListener(EDIT_RATE_DIALOG_TAG)
    }

    override fun onActionReceive(action: Action) {
        viewModel.onActionReceive(action)
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
            viewModel.onPullDownRefreshed()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalListStateFlow.collect { state ->
                    binding.refresh.isRefreshing = state.isPullDownRefreshing
                    if (state.ribbon.isNotEmpty()) {
                        binding.ribbon.populate(
                            list = state.ribbon,
                            listener = { id ->
                                viewModel.onRibbonItemSelected(id = id)
                            }
                        )
                    }
                    adapter.submitList(state.items) {
                        if (state.events.contains(ScrollToTop)) {
                            binding.personalList.scrollToPosition(0)
                            viewModel.onEventConsumed(ScrollToTop)
                        }
                    }

                    state.events.forEach { event ->
                        when (event) {
                            is NavigateToSortFilter -> {
                                val dialog =
                                    SortBottomSheetDialog.newInstance(event.currentSort)
                                dialog.show(
                                    parentFragmentManager,
                                    SortBottomSheetDialog.TAG
                                )
                            }

                            is NavigateToEditRate -> {
                                val dialog =
                                    EditRateBottomSheetDialog.newInstance(
                                        rateId = event.rateId,
                                        tag = EDIT_RATE_DIALOG_TAG,
                                    )
                                dialog.show(
                                    parentFragmentManager,
                                    EDIT_RATE_DIALOG_TAG,
                                )
                            }
                            is ConsumableEvent -> {
                                eventConsumer.consume(event)
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

    companion object {
        private const val EDIT_RATE_DIALOG_TAG = "personal_list_edit_rate_dialog"
    }

}
