package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem

class AnimeListAdapter(
    actionListener: ActionListener,
    loadMoreCallback: (() -> Unit)
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(animeAdapterDelegate(actionListener = actionListener)),
    loadMoreCallback = loadMoreCallback,
)
