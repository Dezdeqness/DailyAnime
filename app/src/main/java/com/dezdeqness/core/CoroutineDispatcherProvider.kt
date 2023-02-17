package com.dezdeqness.core

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

}
