package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.EditRateModule
import com.dezdeqness.presentation.features.editrate.EditRateBottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [EditRateModule::class])
interface EditRateComponent {

    @Subcomponent.Builder
    interface Builder {

        fun userRateModule(module: UserRateModule): Builder

        fun build(): EditRateComponent
    }

    fun inject(fragment: EditRateBottomSheetDialog)
}

@Module
class UserRateModule(private val rateId: Long) {
    @Named("rateId")
    @Provides
    fun provideRateId() = rateId
}
