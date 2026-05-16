package com.dezdeqness.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TopicsArgsModule(private val forumType: String) {

    @Named("forumType")
    @Provides
    fun provideForumType(): String = forumType
}
