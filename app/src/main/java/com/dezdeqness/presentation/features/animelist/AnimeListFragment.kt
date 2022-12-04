package com.dezdeqness.presentation.features.animelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dezdeqness.R
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentAnimeListBinding
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.ui.GridSpacingItemDecoration
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimeListFragment : BaseFragment() {

    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter: AnimeListAdapter by lazy {
        AnimeListAdapter(
            listener = { animeId ->
                findNavController().navigate(
                    R.id.animeDetailsFragment,
                    bundleOf("animeId" to animeId),
                )
            },
            loadMoreCallback = {
                viewModel.onLoadMore()
            },
        )
    }

    private val viewModel: AnimeViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AnimeSearchFilterBottomSheetDialog.TAG) { _, bundle ->
            val filtersList =
                bundle.getParcelableArrayList<AnimeSearchFilter>(AnimeSearchFilterBottomSheetDialog.RESULT)
                    .orEmpty()
            viewModel.applyFilter(filtersList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity()
            .application
            .getComponent()
            .animeComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        setupRefreshLayout()
        setupRecyclerView()
        setupObservers()
        setupFab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(AnimeSearchFilterBottomSheetDialog.TAG)
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.onRefreshSwiped()
        }
    }

    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onQueryChanged(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    viewModel.onQueryEmpty()
                }
                return false
            }

        })
    }

    private fun setupFab() {
        binding.filterAction.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(AnimeSearchFilterBottomSheetDialog.TAG) == null) {
                viewModel.onFabClicked()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.listAnime.adapter = adapter
        // TODO: Dynamic columns
        binding.listAnime.addItemDecoration(GridSpacingItemDecoration())
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeStateFlow.collect { state ->

                    binding.refresh.isRefreshing = state.isRefreshing

                    adapter.submitList(state.list, state.hasNextPage) {
                        if (state.events.contains(Event.ScrollToTop)) {
                            binding.listAnime.scrollToPosition(0)
                            viewModel.onEventConsumed(Event.ScrollToTop)
                        }
                    }
                    state.events.forEach { event ->
                        when (event) {
                            is Event.NavigateToFilter -> {
                                val dialog =
                                    AnimeSearchFilterBottomSheetDialog.newInstance(event.filters)
                                dialog.show(
                                    parentFragmentManager,
                                    AnimeSearchFilterBottomSheetDialog.TAG
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
}
