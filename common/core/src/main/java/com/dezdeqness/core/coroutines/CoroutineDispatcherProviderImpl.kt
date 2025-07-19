package com.dezdeqness.core.coroutines

import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {

    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.IO

    override fun computation() = Dispatchers.Default

}
