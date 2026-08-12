package com.dezdeqness.architecture.store

import kotlin.reflect.KClass

abstract class EventMapper<Event : Any>(
    val eventType: KClass<Event>,
) {

    abstract fun map(event: Event): Any
}
