package com.dezdeqness.core

import com.dezdeqness.advancedrecycler.BaseAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem

class SingleListAdapter(delegate: BaseAdapterDelegate<AdapterItem>) :
    DelegateAdapter<AdapterItem>(
        adapterDelegateList = listOf(delegate),
    )
