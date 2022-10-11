package com.dezdeqness.core

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.advancedrecycler.AdapterDelegateManager
import com.dezdeqness.advancedrecycler.BaseAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

abstract class DelegateAdapter<T : Any>(adapterDelegateList: List<BaseAdapterDelegate<T>>) :
    ListAdapter<AdapterItem, RecyclerView.ViewHolder>(AdapterItemDiffUtil()) {

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
        manager.onBindViewHolder(holder, getItem(position) as T, listOf())

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = manager.onBindViewHolder(holder, getItem(position) as T, payloads)

}
