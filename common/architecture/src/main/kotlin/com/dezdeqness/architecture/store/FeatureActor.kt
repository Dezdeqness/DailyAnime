package com.dezdeqness.architecture.store

import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

abstract class FeatureActor<C : Any, E : Any>(
    val commandType: KClass<C>,
) {

    abstract fun execute(command: C): Flow<E>
}
