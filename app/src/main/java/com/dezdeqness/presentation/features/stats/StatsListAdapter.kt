package com.dezdeqness.presentation.features.stats

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.features.stats.recyclerview.statsHeaderAdapterDelegate
import com.dezdeqness.presentation.features.stats.recyclerview.statsValueAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

class StatsListAdapter : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        statsHeaderAdapterDelegate(),
        statsValueAdapterDelegate()
    ),
)
