package com.dezdeqness.core

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.advancedrecycler.AdapterDelegateManager
import com.dezdeqness.advancedrecycler.BaseAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

abstract class DelegateAdapter<T : Any>(
    adapterDelegateList: List<BaseAdapterDelegate<T>>,
    loadMoreCallback: (() -> Unit)? = null
) :
    ListAdapter<AdapterItem, RecyclerView.ViewHolder>(AdapterItemDiffUtil()) {

    private val paginationHelper = PaginationHelper(loadMoreCallback)
    private val manager = AdapterDelegateManager<T>()

    init {
        adapterDelegateList.forEach { delegate ->
            manager.addDelegate(delegate)
        }
    }

    override fun getItemViewType(position: Int) = manager.getItemViewType(getItem(position) as T)

    override fun getItemCount() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        manager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        manager.onBindViewHolder(holder, getItem(position) as T, listOf()).also {
            paginationHelper.loadMore(position, currentList.size)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = manager.onBindViewHolder(holder, getItem(position) as T, payloads).also {
        paginationHelper.loadMore(position, currentList.size)
    }

    fun submitList(list: List<AdapterItem>, hasNextPage: Boolean, commitCallback: Runnable) {
        this.submitList(list, commitCallback)
        paginationHelper.setLoadingState(hasNextPage)
    }

}
