package com.dezdeqness.feature.auth.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.feature.auth.data.AccountSessionDao
import com.dezdeqness.feature.auth.data.AuthDatabase
import com.dezdeqness.feature.auth.data.SessionManagerImpl
import com.dezdeqness.feature.auth.presentation.AuthorizationViewModel
import com.dezdeqness.foundation.di.SourceScope
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

    companion object {

        @SourceScope
        @Provides
        internal fun provideAuthDatabase(context: Context): AuthDatabase =
            AuthDatabase.build(context)

        @Provides
        internal fun provideAccountSessionDao(database: AuthDatabase): AccountSessionDao =
            database.accountSessionDao()
    }

    @Binds
    @SourceScope
    internal abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    internal abstract fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel
}
