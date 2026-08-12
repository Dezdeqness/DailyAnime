package com.dezdeqness.architecture.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import money.vivid.elmslie.core.store.Actor

class CompositeActor<Command : Any, Event : Any>(
    private val plugins: Set<FeatureActor<*, Event>>,
) : Actor<Command, Event>() {

    @Suppress("UNCHECKED_CAST")
    override fun execute(command: Command): Flow<Event> {
        val plugin = plugins.firstOrNull { it.commandType.isInstance(command) }
            ?: return emptyFlow()

        return (plugin as FeatureActor<Any, Event>).execute(command)
    }
}
