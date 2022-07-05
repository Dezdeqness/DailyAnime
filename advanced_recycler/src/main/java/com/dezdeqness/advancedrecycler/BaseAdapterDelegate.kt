package com.dezdeqness.advancedrecycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterDelegate<MainModel : Any>(open val modelClass: Class<out MainModel>) {

    fun isForViewType(model: MainModel) = model.javaClass == modelClass

    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        model: MainModel,
        payloads: List<Any>
    )

}
