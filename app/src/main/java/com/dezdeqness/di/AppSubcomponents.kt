package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.*
import dagger.Module

@Module(
    subcomponents = [
        AnimeComponent::class,
        AnimeDetailsComponent::class,
        AnimeChronologyComponent::class,
        AnimeSearchFilterComponent::class,
        AnimeStatsComponent::class,
        AuthorizationComponent::class,
        ProfileComponent::class,
        PersonalListComponent::class,
        EditRateComponent::class,
        CalendarComponent::class,
        AnimeSimilarComponent::class,
        UnauthorizedHostComponent::class,
        UnauthorizedComponent::class,
    ]
)
class AppSubcomponents
