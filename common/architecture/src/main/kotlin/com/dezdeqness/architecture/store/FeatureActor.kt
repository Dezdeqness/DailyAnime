package com.dezdeqness.architecture.store

import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

abstract class FeatureActor<Command : Any, out Event : Any>(
    val commandType: KClass<Command>,
) {

    abstract fun execute(command: Command): Flow<Event>
}
