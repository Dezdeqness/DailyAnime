package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.core.DelegateAdapter
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.features.calendar.recyclerview.calendarListDelegate
import com.dezdeqness.presentation.models.AdapterItem

class CalendarAdapter(
    actionListener: ActionListener,
) : DelegateAdapter<AdapterItem>(
    adapterDelegateList = listOf(
        calendarListDelegate(
            actionListener = actionListener,
        )
    )
)
