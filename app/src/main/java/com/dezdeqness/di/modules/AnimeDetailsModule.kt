package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.presentation.features.details.AnimeDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [AnimeModule::class, PersonalModule::class, CharacterModule::class])
abstract class AnimeDetailsModule {

    companion object {

        @Provides
        fun provideGetAnimeDetailsUseCase(
            animeRepository: AnimeRepository,
            authRepository: AuthRepository,
            userRatesRepository: UserRatesRepository,
        ) = GetAnimeDetailsUseCase(
            animeRepository = animeRepository,
            authRepository = authRepository,
            userRatesRepository = userRatesRepository,
        )

        @Provides
        fun provideCreateOrUpdateUserRareUseCase(userRatesRepository: UserRatesRepository) =
            CreateOrUpdateUserRateUseCase(
                userRatesRepository = userRatesRepository,
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(AnimeDetailsViewModel::class)
    abstract fun bindAnimeDetailsViewModel(viewModel: AnimeDetailsViewModel): ViewModel

}
