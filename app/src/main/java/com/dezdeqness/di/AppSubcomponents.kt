package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.*
import dagger.Module

@Module(
    subcomponents = [
        AnimeComponent::class,
        AnimeDetailsComponent::class,
        AnimeSearchFilterComponent::class,
        AuthorizationComponent::class,
        ProfileComponent::class,
        PersonalListComponent::class,
        EditRateComponent::class,
        CalendarComponent::class,
        UnauthorizedHostComponent::class,
        UnauthorizedComponent::class,
    ]
)
class AppSubcomponents
