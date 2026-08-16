package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.anime.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.contract.userrate.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.anime.usecases.GetAnimeDetailsUseCaseImpl
import com.dezdeqness.domain.userrate.usecases.CreateOrUpdateUserRateUseCaseImpl
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
    abstract fun bindGetAnimeDetailsUseCase(getAnimeDetailsUseCase: GetAnimeDetailsUseCaseImpl): GetAnimeDetailsUseCase

    @Binds
    abstract fun bindCreateOrUpdateUserRateUseCase(
        createOrUpdateUserRateUseCase: CreateOrUpdateUserRateUseCaseImpl,
    ): CreateOrUpdateUserRateUseCase

    @Binds
    @IntoMap
    @AssistedViewModelKey(AnimeDetailsViewModel::class)
    abstract fun bindAnimeDetailsViewModelFactory(
        factory: AnimeDetailsViewModel.Factory,
    ): AssistedViewModelFactory<out ViewModel>
}
