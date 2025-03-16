package com.dezdeqness.presentation.action

import com.dezdeqness.presentation.event.AnimeDetails
import com.dezdeqness.presentation.event.EventListener
import com.dezdeqness.presentation.event.OpenCalendarTab
import com.dezdeqness.presentation.event.OpenVideo

class ActionConsumer {

    private var eventListener: EventListener? = null

    fun attachListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun detachListener() {
        this.eventListener = null
    }

    fun consume(action: Action) {
        when (action) {
            is Action.AnimeClick -> {
                eventListener?.onEventReceive(
                    AnimeDetails(
                        animeId = action.animeId,
                        title = action.title
                    )
                )
            }
            is Action.VideoClick -> {
                eventListener?.onEventReceive(OpenVideo(url = action.url))
            }
            is Action.CalendarHeaderClicked -> {
                eventListener?.onEventReceive(OpenCalendarTab)
            }
        }
    }

}
