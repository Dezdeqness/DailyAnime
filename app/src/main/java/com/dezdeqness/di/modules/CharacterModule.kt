package com.dezdeqness.di.modules

import com.dezdeqness.feature.details.character.di.CharacterModule as FeatureCharacterModule
import dagger.Module

@Module(includes = [FeatureCharacterModule::class])
abstract class CharacterModule
