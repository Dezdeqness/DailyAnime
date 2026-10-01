package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.CompositeActor
import com.dezdeqness.architecture.store.CompositeReducer
import com.dezdeqness.architecture.store.FeatureActor
import com.dezdeqness.architecture.store.FeatureReducer
import com.dezdeqness.architecture.store.paging.PagingActor
import com.dezdeqness.architecture.store.paging.PagingCommand
import com.dezdeqness.architecture.store.paging.PagingCommandFactory
import com.dezdeqness.architecture.store.paging.PagingReducer
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Qualifier
import money.vivid.elmslie.core.store.ElmStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchStore

@Module
object SearchStoreModule {

    @SearchStore
    @IntoSet
    @Provides
    fun providePagingReducer(): FeatureReducer<*, State, Effect, Any> =
        PagingReducer(commandFactory = PagingCommandFactory { params -> PagingCommand.LoadPage(params) })

    @SearchStore
    @IntoSet
    @Provides
    fun provideSearchReducer(reducer: SearchReducer): FeatureReducer<*, State, Effect, Any> = reducer

    @SearchStore
    @IntoSet
    @Provides
    fun providePageLoadedReducer(reducer: SearchPageLoadedReducer): FeatureReducer<*, State, Effect, Any> = reducer

    @SearchStore
    @IntoSet
    @Provides
    fun provideLoadFailedReducer(reducer: SearchLoadFailedReducer): FeatureReducer<*, State, Effect, Any> = reducer

    @SearchStore
    @IntoSet
    @Provides
    fun providePagingActor(loader: SearchPageLoader): FeatureActor<*, Any> = PagingActor(loader = loader)

    @SearchStore
    @IntoSet
    @Provides
    fun provideSearchActor(actor: SearchActor): FeatureActor<*, Any> = actor

    @SearchStore
    @Provides
    fun provideStore(
        @SearchStore reducers: Set<@JvmSuppressWildcards FeatureReducer<*, State, Effect, Any>>,
        @SearchStore actors: Set<@JvmSuppressWildcards FeatureActor<*, Any>>,
    ): ElmStore<Any, State, Effect, Any> = ElmStore(
        initialState = State(),
        reducer = CompositeReducer(reducers),
        actor = CompositeActor(actors),
    )
}
