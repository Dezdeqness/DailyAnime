package com.dezdeqness.di.source

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.data.analytics.AnalyticsManager
import com.dezdeqness.di.subcomponents.AchievementsSubcomponent
import com.dezdeqness.di.subcomponents.AnimeChronologyComponent
import com.dezdeqness.di.subcomponents.AnimeComponent
import com.dezdeqness.di.subcomponents.AnimeDetailsFeatureComponent
import com.dezdeqness.di.subcomponents.AnimeSimilarComponent
import com.dezdeqness.di.subcomponents.AnimeStatsComponent
import com.dezdeqness.di.subcomponents.AuthorizationComponent
import com.dezdeqness.di.subcomponents.CalendarComponent
import com.dezdeqness.di.subcomponents.CharacterDetailsFeatureComponent
import com.dezdeqness.di.subcomponents.FavouriteSubcomponent
import com.dezdeqness.di.subcomponents.ForumComponent
import com.dezdeqness.di.subcomponents.HistoryComponent
import com.dezdeqness.di.subcomponents.HomeComponent
import com.dezdeqness.di.subcomponents.MainComponent
import com.dezdeqness.di.subcomponents.OnboardingSubcomponent
import com.dezdeqness.di.subcomponents.PersonDetailsFeatureComponent
import com.dezdeqness.di.subcomponents.PersonalListComponent
import com.dezdeqness.di.subcomponents.PersonalListSearchComponent
import com.dezdeqness.di.subcomponents.PersonalListTabComponent
import com.dezdeqness.di.subcomponents.ProfileComponent
import com.dezdeqness.di.subcomponents.RoutingComponent
import com.dezdeqness.di.subcomponents.ScreenshotsViewerComponent
import com.dezdeqness.di.subcomponents.SelectGenresSubcomponent
import com.dezdeqness.di.subcomponents.SettingsComponent
import com.dezdeqness.di.subcomponents.StatsComponent
import com.dezdeqness.di.subcomponents.TopicDetailsComponent
import com.dezdeqness.di.subcomponents.TopicsComponent
import com.dezdeqness.di.subcomponents.UserRateComponent

interface SourceComponent {
    val sourceConfig: SourceConfig

    val sessionManager: SessionManager

    val analyticsManager: AnalyticsManager

    fun animeComponent(): AnimeComponent.Factory

    fun profileComponent(): ProfileComponent.Factory

    fun authorizationComponent(): AuthorizationComponent.Factory

    fun animeDetailsFeatureComponent(): AnimeDetailsFeatureComponent.Factory

    fun characterDetailsFeatureComponent(): CharacterDetailsFeatureComponent.Factory

    fun personDetailsFeatureComponent(): PersonDetailsFeatureComponent.Factory

    fun personalListComponent(): PersonalListComponent.Factory

    fun personalListTabComponent(): PersonalListTabComponent.Factory

    fun personalListSearchComponent(): PersonalListSearchComponent.Factory

    fun editRateComponent(): UserRateComponent.Builder

    fun calendarComponent(): CalendarComponent.Factory

    fun historyComponent(): HistoryComponent.Factory

    fun settingsComponent(): SettingsComponent.Factory

    fun statsComponent(): StatsComponent.Factory

    fun animeStatsComponent(): AnimeStatsComponent.Builder

    fun animeSimilarComponent(): AnimeSimilarComponent.Builder

    fun animeChronologyComponent(): AnimeChronologyComponent.Builder

    fun mainComponent(): MainComponent.Factory

    fun routingComponent(): RoutingComponent.Factory

    fun screenshotsViewerComponent(): ScreenshotsViewerComponent.Builder

    fun homeComponent(): HomeComponent.Factory

    fun achievementsComponent(): AchievementsSubcomponent.Builder

    fun favouriteComponent(): FavouriteSubcomponent.Builder

    fun selectGenresComponent(): SelectGenresSubcomponent.Factory

    fun onboardingComponent(): OnboardingSubcomponent.Factory

    fun topicsComponent(): TopicsComponent.Builder

    fun forumComponent(): ForumComponent.Factory

    fun topicDetailsComponent(): TopicDetailsComponent.Factory
}
