package com.dezdeqness.advancedrecycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.collection.forEach
import androidx.recyclerview.widget.RecyclerView
import kotlin.NoSuchElementException

class AdapterDelegateManager<MainModel : Any> {

    private val listOfDelegates =
        SparseArrayCompat<BaseAdapterDelegate<MainModel>>()

    fun addDelegate(delegate: BaseAdapterDelegate<MainModel>) {
        val viewType = listOfDelegates.size()
        listOfDelegates.put(viewType, delegate)
    }

    fun getItemViewType(item: MainModel): Int {
        listOfDelegates.forEach { key, value ->
            if (value.isForViewType(item)) {
                return key
            }
        }

        throw NoSuchElementException("No AdapterDelegate added that matches item $item")
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        listOfDelegates.get(viewType)?.onCreateViewHolder(parent)
            ?: throw NoSuchElementException("No AdapterDelegate added that matches type $viewType")

    fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: MainModel,
        payloads: List<Any>
    ) {
        listOfDelegates.get(viewHolder.itemViewType)?.onBindViewHolder(viewHolder, item, payloads)
    }

}
