package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.forum.usecases.GetForumsUseCase
import com.dezdeqness.domain.forum.usecases.GetForumsUseCaseImpl
import com.dezdeqness.feature.forum.di.ForumModule as FeatureForumModule
import com.dezdeqness.feature.forum.presentation.ForumViewModel
import com.dezdeqness.feature.forum.presentation.store.ForumActor
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Command
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Effect
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Event
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.State
import com.dezdeqness.feature.forum.presentation.store.forumReducer
import com.dezdeqness.feature.topics.di.TopicModule
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [FeatureForumModule::class, TopicModule::class])
abstract class ForumModule {

    companion object {

        @Provides
        fun provideForumStore(actor: ForumActor): ElmStore<Event, State, Effect, Command> = ElmStore(
            initialState = State(),
            reducer = forumReducer,
            actor = actor,
        )
    }

    @Binds
    abstract fun bindGetForumsUseCase(getForumsUseCase: GetForumsUseCaseImpl): GetForumsUseCase

    @Binds
    @IntoMap
    @ViewModelKey(ForumViewModel::class)
    abstract fun bindForumViewModel(viewModel: ForumViewModel): ViewModel
}
