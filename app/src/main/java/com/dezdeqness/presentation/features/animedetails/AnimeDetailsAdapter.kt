package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.features.animedetails.recyclerview.briefInfoAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.cellsAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.descriptionAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.headerItemAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.moreInfoAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.relatesAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.roleAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.screenshotsAdapterListDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.spacerAdapterDelegate
import com.dezdeqness.presentation.features.animedetails.recyclerview.videosAdapterListDelegate
import com.dezdeqness.presentation.models.AdapterItem

class AnimeDetailsAdapter(
    actionListener: ActionListener,
    onStatsClicked: () -> Unit,
    onChronologyClicked: () -> Unit,
    onSimilarClicked: () -> Unit,
    onScreenShotClicked: (String) -> Unit,
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        headerItemAdapterDelegate(),
        briefInfoAdapterListDelegate(),
        relatesAdapterListDelegate(actionListener),
        roleAdapterListDelegate(),
        screenshotsAdapterListDelegate(onScreenShotClicked),
        videosAdapterListDelegate(actionListener),
        descriptionAdapterDelegate(),
        cellsAdapterDelegate(),
        spacerAdapterDelegate(),
        moreInfoAdapterDelegate(
            onStatsClicked = onStatsClicked,
            onChronologyClicked = onChronologyClicked,
            onSimilarClicked = onSimilarClicked,
        ),
    )
)
