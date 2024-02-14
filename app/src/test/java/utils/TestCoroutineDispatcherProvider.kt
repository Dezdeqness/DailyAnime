package utils

import com.dezdeqness.core.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Unconfined

    override fun io() = Dispatchers.Unconfined

    override fun computation() = Dispatchers.Unconfined
}
