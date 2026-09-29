package com.dezdeqness.di.source

import android.content.Context
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.data.analytics.impl.AnalyticsManagerImpl
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.foundation.di.SourceScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides

@Module
object SourceSharedModule {

    @SourceScope
    @Provides
    fun provideTokenManager(context: Context) = TokenManager(context = context)

    @SourceScope
    @Provides
    fun provideAnalyticsManager(
        userRepository: UserRepository,
        firebaseAnalytics: FirebaseAnalytics,
    ): AnalyticsManager = AnalyticsManagerImpl(
        userRepository = userRepository,
        firebaseAnalytics = firebaseAnalytics,
    )
}
