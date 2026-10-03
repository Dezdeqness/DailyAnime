package com.dezdeqness.foundation.test

import com.dezdeqness.architecture.store.CompositeReducer
import com.dezdeqness.architecture.store.FeatureReducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import money.vivid.elmslie.core.config.ElmslieConfig
import money.vivid.elmslie.core.store.Actor
import money.vivid.elmslie.core.store.ElmStore
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class StoreTest {

    protected val dispatcher = StandardTestDispatcher()

    @Before
    fun setupDispatchers() {
        Dispatchers.setMain(dispatcher)
        ElmslieConfig.elmDispatcher { dispatcher }
    }

    @After
    fun resetDispatchers() {
        ElmslieConfig.elmDispatcher { Dispatchers.Default }
        Dispatchers.resetMain()
    }
}

class PluginStore<State : Any, Effect : Any>(
    initialState: State,
    plugin: FeatureReducer<*, State, Effect, Any>,
) {
    private val executedCommands = Channel<Any>(Channel.UNLIMITED)

    private val store = ElmStore(
        initialState = initialState,
        reducer = CompositeReducer(setOf(plugin)),
        actor = object : Actor<Any, Any>() {
            override fun execute(command: Any): Flow<Any> {
                executedCommands.trySend(command)
                return emptyFlow()
            }
        },
    )

    val commands: Flow<Any> = executedCommands.receiveAsFlow()

    val effects: Flow<Effect> = store.effects

    val state: State get() = store.states.value

    fun accept(event: Any) = store.accept(event)
}
