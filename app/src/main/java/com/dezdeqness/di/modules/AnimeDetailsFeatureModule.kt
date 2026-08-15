package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.userrate.repository.UserRatesRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.feature.details.anime.presentation.AnimeDetailsViewModel
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsActor
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.anime.presentation.store.animeDetailsReducer
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.di.AssistedViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [AnimeModule::class, PersonalModule::class])
abstract class AnimeDetailsFeatureModule {

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
        fun provideCreateOrUpdateUserRateUseCase(userRatesRepository: UserRatesRepository) =
            CreateOrUpdateUserRateUseCase(
                userRatesRepository = userRatesRepository,
            )

        @Provides
        fun provideAnimeDetailsStore(
            actor: AnimeDetailsActor,
        ): ElmStore<
            AnimeDetailsNamespace.Event,
            AnimeDetailsNamespace.State,
            AnimeDetailsNamespace.Effect,
            AnimeDetailsNamespace.Command,
            > =
            ElmStore(
                initialState = AnimeDetailsNamespace.State(),
                reducer = animeDetailsReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @AssistedViewModelKey(AnimeDetailsViewModel::class)
    abstract fun bindAnimeDetailsViewModelFactory(
        factory: AnimeDetailsViewModel.Factory,
    ): AssistedViewModelFactory<out ViewModel>
}
