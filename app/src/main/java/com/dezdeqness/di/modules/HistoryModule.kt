package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.history.usecases.GetHistoryUseCase
import com.dezdeqness.contract.history.usecases.GetLatestHistoryItemUseCase
import com.dezdeqness.domain.history.usecases.GetHistoryUseCaseImpl
import com.dezdeqness.domain.history.usecases.GetLatestHistoryItemUseCaseImpl
import com.dezdeqness.feature.history.presentation.HistoryViewModel
import com.dezdeqness.feature.history.presentation.store.HistoryActor
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Command
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Effect
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.Event
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace.State
import com.dezdeqness.feature.history.presentation.store.historyReducer
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module
abstract class HistoryModule {

    companion object {

        @Provides
        fun provideHistoryStore(actor: HistoryActor): ElmStore<Event, State, Effect, Command> = ElmStore(
            initialState = State(),
            reducer = historyReducer,
            actor = actor,
        )
    }

    @Binds
    abstract fun bindGetHistoryUseCase(getHistoryUseCase: GetHistoryUseCaseImpl): GetHistoryUseCase

    @Binds
    abstract fun bindGetLatestHistoryItemUseCase(
        getLatestHistoryItemUseCase: GetLatestHistoryItemUseCaseImpl,
    ): GetLatestHistoryItemUseCase

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel
}
