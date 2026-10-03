package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Command
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchActorTest {

    private val settingsRepository = mockk<SettingsRepository>()
    private val actor = SearchActor(settingsRepository)

    @Test
    fun `WHEN adult content setting changes SHOULD emit an event per real change, skipping the initial value`() =
        runTest {
            every { settingsRepository.observePreference(AdultContentPreference) } returns
                flowOf(false, false, true, true, false)

            val events = actor.execute(Command.ObserveAdultContent).toList()

            assertEquals(listOf(Event.AdultContentChanged, Event.AdultContentChanged), events)
        }
}
