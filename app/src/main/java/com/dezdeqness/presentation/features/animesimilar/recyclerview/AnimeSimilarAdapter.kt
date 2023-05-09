package com.dezdeqness.presentation.features.animesimilar.recyclerview

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem

class AnimeSimilarAdapter(
    actionListener: ActionListener,
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        similarAdapterDelegate(actionListener = actionListener)
    ),
)
