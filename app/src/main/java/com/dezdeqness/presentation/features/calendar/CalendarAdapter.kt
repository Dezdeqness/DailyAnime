package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.features.calendar.recyclerview.calendarListDelegate
import com.dezdeqness.presentation.models.AdapterItem

class CalendarAdapter(
    onAnimeClickListener: (Long) -> Unit,
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        calendarListDelegate(
            onAnimeClickListener = onAnimeClickListener,
        )
    )
)
