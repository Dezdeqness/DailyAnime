package com.dezdeqness.architecture.store.paging

import com.dezdeqness.architecture.store.FeatureActor
import com.dezdeqness.architecture.util.Switcher
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PagingActor<Item : Any, Params : Any, Error : Any>(
    private val loader: PageLoader<Item, Params, Error>,
) : FeatureActor<PagingCommand<Params>, PagingEvent<Item, Params, Error>>(
    @Suppress("UNCHECKED_CAST") (PagingCommand::class as KClass<PagingCommand<Params>>),
) {

    private val switcher = Switcher()

    override fun execute(command: PagingCommand<Params>): Flow<PagingEvent<Item, Params, Error>> =
        when (command) {
            is PagingCommand.LoadPage -> switcher.switch {
                flow {
                    val event: PagingEvent<Item, Params, Error> =
                        when (val result = loader.load(command.params)) {
                            is PageResult.Success ->
                                PagingEvent.PageLoaded(result.items, result.nextParams)
                            is PageResult.Failure ->
                                PagingEvent.LoadFailed(result.error)
                        }
                    emit(event)
                }
            }
        }
}
