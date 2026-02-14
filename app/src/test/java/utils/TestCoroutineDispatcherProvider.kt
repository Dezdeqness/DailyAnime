package utils

import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.Main

    override fun computation() = Dispatchers.Main
}
