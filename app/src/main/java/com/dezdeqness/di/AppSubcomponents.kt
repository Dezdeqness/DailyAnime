package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeChronologyComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import com.dezdeqness.di.subcomponents.AnimeStatsComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.EditRateComponent
import com.dezdeqness.di.subcomponents.CalendarComponent
import com.dezdeqness.di.subcomponents.AnimeSimilarComponent
import com.dezdeqness.di.subcomponents.MainComponent
import com.dezdeqness.di.subcomponents.UnauthorizedHostComponent
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
        MainComponent::class,
    ]
)
class AppSubcomponents
