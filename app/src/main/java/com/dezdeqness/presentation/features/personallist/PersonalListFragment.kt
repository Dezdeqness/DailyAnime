package com.dezdeqness.presentation.features.personallist

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentPersonalListBinding
import com.dezdeqness.di.AppComponent
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.event.ConsumableEvent
import com.dezdeqness.presentation.event.EventConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.OpenMenuPopupFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.features.editrate.EditRateBottomSheetDialog
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import kotlinx.coroutines.launch


class PersonalListFragment : BaseFragment<FragmentPersonalListBinding>(), ActionListener {

    private val adapter: PersonalListAdapter by lazy {
        PersonalListAdapter(
            actionListener = this,
            onEditRateClicked = { editRate ->
                viewModel.onEditRateClicked(editRate)
            }
        )
    }

    private val eventConsumer: EventConsumer by lazy {
        EventConsumer(
            fragment = this,
            context = this.requireContext(),
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
        setFragmentResultListener(EDIT_RATE_DIALOG_TAG) { _, bundle ->
            val userRate = bundle.getParcelable<EditRateUiModel>(EditRateBottomSheetDialog.RESULT)
            viewModel.onUserRateChanged(userRate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupSearchView()
        setupObservers()
        setupMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
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

    private fun setupMenu() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_personal_list)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_filter -> {
                        viewModel.onFilterButtonClicked()
                    }
                }
                true
            }
            menu?.findItem(R.id.action_filter)?.isVisible = false
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.personalListStateFlow.collect { state ->
                    binding.refresh.isRefreshing = state.isPullDownRefreshing

                    setupShareButton(state = state)

                    val isLoadingStateShowing =
                        if (state.items.isEmpty() && state.isEmptyStateShowing.not()) {
                            state.isInitialLoadingIndicatorShowing
                        } else {
                            false
                        }

                    binding.recycler.setLoadingState(
                        isLoadingStateShowing = isLoadingStateShowing,
                    )

                    binding.recycler.setEmptyState(
                        isEmptyStateShowing = state.isEmptyStateShowing,
                    )

                    if (state.ribbon.isNotEmpty()) {
                        binding.ribbon.populate(
                            list = state.ribbon,
                            listener = { id ->
                                viewModel.onRibbonItemSelected(id = id)
                            }
                        )
                    }
                    adapter.submitList(state.items) {
                        if (state.isScrollNeed) {
                            viewModel.onScrollNeed()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ScrollToTop -> {
                            binding.recycler.scrollToPosition(0)
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

                        is OpenMenuPopupFilter -> {
                            openPopupMenu()
                        }

                        is ConsumableEvent -> {
                            eventConsumer.consume(event)
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun setupShareButton(state: PersonalListState) {
        if (state.items.isNotEmpty()) {
            binding.toolbar.menu?.findItem(R.id.action_filter)?.let { menuItem ->
                if (menuItem.isVisible.not()) {
                    menuItem.isVisible = true
                }
            }
        }
    }

    private fun openPopupMenu() {
        PopupMenu(requireContext(), binding.toolbar, Gravity.END).apply {
            menuInflater.inflate(R.menu.menu_popup_filter, menu)
            setOnMenuItemClickListener { menuItem ->
                viewModel.onSortChanged(menuItem.titleCondensed.toString())
                true
            }
            show()
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = adapter
    }

    companion object {
        private const val EDIT_RATE_DIALOG_TAG = "personal_list_edit_rate_dialog"
    }

}
