package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.FeatureActor
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Command
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

class SearchActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : FeatureActor<Command, Event>(Command::class) {

    override fun execute(command: Command): Flow<Event> = when (command) {
        Command.ObserveAdultContent ->
            settingsRepository
                .observePreference(AdultContentPreference)
                .distinctUntilChanged()
                .drop(1)
                .map { Event.AdultContentChanged }
    }
}
