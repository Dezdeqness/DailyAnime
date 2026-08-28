package com.dezdeqness.di.modules

import com.dezdeqness.feature.personallist.di.PersonalListFilterModule
import dagger.Module

@Module(includes = [PersonalListFilterModule::class])
abstract class PersonalModule
