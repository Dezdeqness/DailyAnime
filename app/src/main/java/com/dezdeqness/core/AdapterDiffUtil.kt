package com.dezdeqness.core

import androidx.recyclerview.widget.DiffUtil
import com.dezdeqness.presentation.models.AdapterItem

class AdapterItemDiffUtil(
) : DiffUtil.ItemCallback<AdapterItem>() {

    override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem::class == newItem::class && oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem == newItem

    override fun getChangePayload(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem.payload(newItem)
}
