package com.dezdeqness.architecture.store

import kotlin.reflect.KClass
import money.vivid.elmslie.core.store.dsl.ResultBuilder

abstract class FeatureReducer<Event : Any, State : Any, Effect : Any, Command : Any>(
    val eventType: KClass<Event>,
) {

    abstract fun ResultBuilder<State, Effect, Command>.reduce(event: Event)
}
