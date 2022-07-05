package com.dezdeqness.advancedrecycler

import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class CastAdapterDelegate<ChildModel : MainModel, MainModel : Any, ViewHolder : RecyclerView.ViewHolder>(
    override val modelClass: Class<out MainModel>
) : BaseAdapterDelegate<MainModel>(modelClass) {

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        model: MainModel,
        payloads: List<Any>
    ) {
        onBindViewHolder(
            item = model as ChildModel,
            viewHolder = viewHolder as ViewHolder,
            payloads = payloads,
        )
    }

    protected abstract fun onBindViewHolder(
        item: ChildModel,
        viewHolder: ViewHolder,
        payloads: List<Any>
    )
}
