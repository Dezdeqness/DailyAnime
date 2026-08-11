package com.dezdeqness.architecture.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import money.vivid.elmslie.core.store.Actor

class CompositeActor<C : Any, E : Any>(
    private val plugins: Set<FeatureActor<*, E>>,
) : Actor<C, E>() {

    @Suppress("UNCHECKED_CAST")
    override fun execute(command: C): Flow<E> {
        val plugin = plugins.firstOrNull { it.commandType.isInstance(command) }
            ?: return emptyFlow()

        return (plugin as FeatureActor<Any, E>).execute(command)
    }
}
