package com.dezdeqness.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.advancedrecycler.AdapterDelegateManager
import com.dezdeqness.presentation.features.animelist.animeAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

class DelegateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val manager = AdapterDelegateManager<AdapterItem>()
    private val items = mutableListOf<AdapterItem>()

    init {
        manager.addDelegate(animeAdapterDelegate())
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

    fun setItems(list: List<AdapterItem>) {
        with(items) {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

}
