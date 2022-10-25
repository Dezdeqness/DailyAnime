package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.models.AdapterItem

class AnimeListAdapter(
    listener: (Long) -> Unit,
    loadMoreCallback: (() -> Unit)
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(animeAdapterDelegate(listener)),
    loadMoreCallback = loadMoreCallback,
)
