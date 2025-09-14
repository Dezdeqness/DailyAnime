package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.history.repository.HistoryRepository
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.domain.usecases.GetLatestHistoryItemUseCase
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.history.HistoryViewModel
import com.dezdeqness.presentation.features.history.store.HistoryActor
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Effect
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Event
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.State
import com.dezdeqness.presentation.features.history.store.historyReducer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module
abstract class HistoryModule {

    companion object {

        @Provides
        fun provideGetHistoryUseCase(historyRepository: HistoryRepository) =
            GetHistoryUseCase(historyRepository = historyRepository)

        @Provides
        fun provideGetLatestHistoryItemUseCase(
            historyRepository: HistoryRepository,
            authRepository: AuthRepository,
        ) =
            GetLatestHistoryItemUseCase(
                historyRepository = historyRepository,
                authRepository = authRepository,
            )

        @Provides
        fun provideHistoryStore(actor: HistoryActor): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = historyReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

}
