package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import dagger.Module

@Module(
    subcomponents = [
        AnimeComponent::class,
        AnimeSearchFilterComponent::class,
    ]
)
class AppSubcomponents
