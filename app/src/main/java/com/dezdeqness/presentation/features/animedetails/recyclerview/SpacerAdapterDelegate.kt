package com.dezdeqness.presentation.features.animedetails.recyclerview

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemSpacerBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.SpacerUiItem

fun spacerAdapterDelegate() =
    adapterDelegateViewBinding<SpacerUiItem, AdapterItem, ItemSpacerBinding>(
        modelClass = SpacerUiItem::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemSpacerBinding.inflate(layoutInflater, parent, false)
        },
        block = {}
    )
