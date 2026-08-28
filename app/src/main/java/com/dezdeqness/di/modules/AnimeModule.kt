package com.dezdeqness.di.modules

import com.dezdeqness.data.anime.di.AnimeDataModule
import dagger.Module

@Module(includes = [PersonalModule::class, AnimeDataModule::class])
abstract class AnimeModule
