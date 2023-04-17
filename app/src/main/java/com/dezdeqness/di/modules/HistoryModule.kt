package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.presentation.features.history.HistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class HistoryModule {

    companion object {
        @Provides
        fun provideGetHistoryUseCase(repository: AccountRepository) =
            GetHistoryUseCase(
                accountRepository = repository,
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindVHistoryViewModel(viewModel: HistoryViewModel): ViewModel

}
