package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.features.animedetails.recyclerview.briefInfoAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.cellsAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.descriptionAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.relatesAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.roleAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.screenshotsAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.videosAdapterListDelegate
import com.dezdeqness.presentation.models.AdapterItem

class AnimeDetailsAdapter : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        briefInfoAdapterListDelegate(),
        relatesAdapterListDelegate(),
        roleAdapterListDelegate(),
        screenshotsAdapterListDelegate(),
        videosAdapterListDelegate(),
        descriptionAdapterDelegate(),
        cellsAdapterDelegate(),
        spacerAdapterDelegate(),
    )
)
