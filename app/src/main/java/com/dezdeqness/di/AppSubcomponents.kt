package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeChronologyComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeStatsComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.CalendarComponent
import com.dezdeqness.di.subcomponents.AnimeSimilarComponent
import com.dezdeqness.di.subcomponents.DebugComponent
import com.dezdeqness.di.subcomponents.MainComponent
import com.dezdeqness.di.subcomponents.ScreenshotsViewerComponent
import com.dezdeqness.di.subcomponents.PersonalListHostComponent
import com.dezdeqness.di.subcomponents.UserRateComponent
import dagger.Module

@Module(
    subcomponents = [
        AnimeComponent::class,
        AnimeDetailsComponent::class,
        AnimeChronologyComponent::class,
        AnimeStatsComponent::class,
        AuthorizationComponent::class,
        ProfileComponent::class,
        PersonalListComponent::class,
        UserRateComponent::class,
        CalendarComponent::class,
        AnimeSimilarComponent::class,
        PersonalListHostComponent::class,
        MainComponent::class,
        ScreenshotsViewerComponent::class,
        DebugComponent::class,
    ]
)
class AppSubcomponents
