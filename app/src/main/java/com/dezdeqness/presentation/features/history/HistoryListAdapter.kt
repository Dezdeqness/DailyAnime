package com.dezdeqness.presentation.features.history

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.models.AdapterItem

class HistoryListAdapter(
    loadMoreCallback: (() -> Unit)
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        historyAdapterDelegate(),
        historyHeaderAdapterDelegate()
    ),
    loadMoreCallback = loadMoreCallback,
)
