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

        val matching = plugins.filter { it.eventType.isInstance(actual) }
        check(matching.isNotEmpty()) { "No FeatureReducer registered for ${actual::class.qualifiedName}" }

        matching.forEach { plugin ->
            with(plugin as FeatureReducer<Any, State, Effect, Command>) { reduce(actual) }
        }
    }
}
