package com.dezdeqness.presentation.features.animechronology.recyclerview

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem

class AnimeChronologyAdapter(
    actionListener: ActionListener,
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        chronologyAdapterDelegate(
            actionListener = actionListener
        )
    ),
)
