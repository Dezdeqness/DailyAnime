package com.dezdeqness.di.modules

import com.dezdeqness.feature.details.person.di.PersonModule as FeaturePersonModule
import dagger.Module

@Module(includes = [FeaturePersonModule::class])
abstract class PersonModule
