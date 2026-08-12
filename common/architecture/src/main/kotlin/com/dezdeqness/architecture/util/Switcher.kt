package com.dezdeqness.architecture.util

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Switcher {

    private var currentChannel: SendChannel<*>? = null
    private val lock = Mutex()

    fun <T : Any> switch(action: () -> Flow<T>): Flow<T> = callbackFlow {
        lock.withLock {
            currentChannel?.close()
            currentChannel = channel
        }
        action().onEach { send(it) }.catch { close(it) }.collect()
        channel.close()
    }
}
