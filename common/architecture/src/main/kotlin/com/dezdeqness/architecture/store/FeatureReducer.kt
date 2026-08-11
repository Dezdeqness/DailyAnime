package com.dezdeqness.architecture.store

import kotlin.reflect.KClass
import money.vivid.elmslie.core.store.dsl.ResultBuilder

abstract class FeatureReducer<E : Any, S : Any, F : Any, C : Any>(
    val eventType: KClass<E>,
) {

    abstract fun ResultBuilder<S, F, C>.reduce(event: E)
}
