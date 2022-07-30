package com.dezdeqness.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.advancedrecycler.AdapterDelegateManager
import com.dezdeqness.advancedrecycler.BaseAdapterDelegate
import com.dezdeqness.presentation.features.animelist.animeAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

abstract class DelegateAdapter<T : Any>(adapterDelegateList: List<BaseAdapterDelegate<T>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val manager = AdapterDelegateManager<T>()
    private val items = mutableListOf<T>()

    init {
        adapterDelegateList.forEach { delegate ->
            manager.addDelegate(delegate)
        }
    }

    override fun getItemViewType(position: Int) = manager.getItemViewType(items[position])

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        manager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        manager.onBindViewHolder(holder, items[position], listOf())

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = manager.onBindViewHolder(holder, items[position], payloads)

    fun setItems(list: List<T>) {
        with(items) {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

}
