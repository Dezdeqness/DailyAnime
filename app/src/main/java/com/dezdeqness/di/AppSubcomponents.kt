package com.dezdeqness.di

import com.dezdeqness.di.subcomponents.EditRateComponent
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsComponent
import com.dezdeqness.di.subcomponents.AnimeSearchFilterComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
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
    ]
)
class AppSubcomponents
