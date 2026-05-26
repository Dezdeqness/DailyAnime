package com.dezdeqness.feature.details.common.presentation

fun interface DetailsEventTranslator<UiEvent, StoreEvent> {
    fun translate(uiEvent: UiEvent): StoreEvent
}
