package com.dezdeqness.presentation.features.animelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dezdeqness.core.BaseFragment
import com.dezdeqness.databinding.FragmentListAnimeBinding
import com.dezdeqness.getComponent
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.ui.GridSpacingItemDecoration
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListAnimeFragment : BaseFragment() {

    private var _binding: FragmentListAnimeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter: AnimeListAdapter by lazy { AnimeListAdapter() }

    private val viewModel: AnimeViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AnimeSearchFilterBottomSheetDialog.TAG) { _, bundle ->
            val filtersList = bundle.getParcelableArrayList<AnimeSearchFilter>(AnimeSearchFilterBottomSheetDialog.RESULT).orEmpty()
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
        _binding = FragmentListAnimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupFab()
    }

    private fun setupFab() {
        binding.filterAction.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(AnimeSearchFilterBottomSheetDialog.TAG) == null) {
                val dialog = AnimeSearchFilterBottomSheetDialog.newInstance()
                dialog.show(parentFragmentManager, AnimeSearchFilterBottomSheetDialog.TAG)
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
                    adapter.setItems(state.list)
                }
            }
        }
    }
}
