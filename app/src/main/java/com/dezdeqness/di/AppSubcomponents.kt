package com.dezdeqness.di

import dagger.Module

@Module(
    subcomponents = [
        AnimeComponent::class,
        AnimeSearchFilterComponent::class,
    ]
)
class AppSubcomponents
