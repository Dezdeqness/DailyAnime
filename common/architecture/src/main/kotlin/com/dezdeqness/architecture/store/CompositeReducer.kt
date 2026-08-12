package com.dezdeqness.architecture.store

import money.vivid.elmslie.core.store.StateReducer

class CompositeReducer<State : Any, Effect : Any, Command : Any>(
    private val plugins: Set<FeatureReducer<*, State, Effect, Command>>,
    private val mappers: Set<EventMapper<*>> = emptySet(),
) : StateReducer<Any, State, Effect, Command>() {

    @Suppress("UNCHECKED_CAST")
    override fun Result.reduce(event: Any) {
        val actual = mappers.firstOrNull { it.eventType.isInstance(event) }
            ?.let { (it as EventMapper<Any>).map(event) }
            ?: event

        val plugin = plugins.firstOrNull { it.eventType.isInstance(actual) }
            ?: error("No FeatureReducer registered for ${actual::class.qualifiedName}")

        with(plugin as FeatureReducer<Any, State, Effect, Command>) { reduce(actual) }
    }
}
