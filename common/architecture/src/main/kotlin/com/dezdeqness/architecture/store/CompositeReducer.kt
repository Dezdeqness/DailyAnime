package com.dezdeqness.architecture.store

import money.vivid.elmslie.core.store.StateReducer

class CompositeReducer<State : Any, Effect : Any, Command : Any>(
    private val plugins: Set<FeatureReducer<*, State, Effect, Command>>,
) : StateReducer<Any, State, Effect, Command>() {

    @Suppress("UNCHECKED_CAST")
    override fun Result.reduce(event: Any) {
        val plugin = plugins.firstOrNull { it.eventType.isInstance(event) }
            ?: error("No FeatureReducer registered for ${event::class.qualifiedName}")

        with(plugin as FeatureReducer<Any, State, Effect, Command>) { reduce(event) }
    }
}
