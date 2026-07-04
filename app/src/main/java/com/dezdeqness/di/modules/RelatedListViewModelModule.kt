package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.details.related.presentation.RelatedListViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RelatedListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RelatedListViewModel::class)
    abstract fun bindRelatedListViewModel(viewModel: RelatedListViewModel): ViewModel
}
