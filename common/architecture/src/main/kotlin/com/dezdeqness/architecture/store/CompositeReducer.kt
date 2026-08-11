package com.dezdeqness.architecture.store

import money.vivid.elmslie.core.store.StateReducer

class CompositeReducer<S : Any, F : Any, C : Any>(
    private val plugins: Set<FeatureReducer<*, S, F, C>>,
) : StateReducer<Any, S, F, C>() {

    @Suppress("UNCHECKED_CAST")
    override fun Result.reduce(event: Any) {
        val plugin = plugins.firstOrNull { it.eventType.isInstance(event) }
            ?: error("No FeatureReducer registered for ${event::class.qualifiedName}")

        with(plugin as FeatureReducer<Any, S, F, C>) { reduce(event) }
    }
}
