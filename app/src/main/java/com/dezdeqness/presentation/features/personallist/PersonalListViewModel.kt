package com.dezdeqness.presentation.features.personallist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.PersonalListComposer
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalListViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val personalListFilterRepository: PersonalListFilterRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private var currentRibbonId: String? = null

    private var query: String? = null

    private var ribbonRaw: FullAnimeStatusesEntity = FullAnimeStatusesEntity(listOf())

    private var userRatesList: List<UserRateEntity> = listOf()

    private val _personalListStateFlow: MutableStateFlow<PersonalListState> =
        MutableStateFlow(PersonalListState())

    val personalListStateFlow: StateFlow<PersonalListState> get() = _personalListStateFlow

    // TODO: Add fetching when app from foreground
    fun loadPersonalList() {
        viewModelScope.launch(context = Dispatchers.IO) {
            accountRepository.getProfileDetails().collect { result ->
                result.onSuccess { details ->

                    ribbonRaw = details.fullAnimeStatusesEntity

                    val ribbon = personalListComposer.composeStatuses(ribbonRaw, currentRibbonId)

                    if (currentRibbonId == null) {
                        if (ribbon.isEmpty()) {
                            return@onSuccess
                        }
                        currentRibbonId = ribbon.first().id
                    }

                    if (ribbon.isEmpty()) {
                        // TODO: Empty state
                    } else {
                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            ribbon = ribbon,
                            isPullDownRefreshing = true,
                        )
                        currentRibbonId?.let {
                            fetchPage(it)
                        }
                    }
                }
                    .onFailure { exception ->
                        Log.d("PersonalListViewModel", exception.toString())
                    }
            }
        }
    }

    fun onRefreshSwiped() {
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            isPullDownRefreshing = true,
        )
        viewModelScope.launch(context = Dispatchers.IO) {
            currentRibbonId?.let {
                fetchPage(it)
            }
        }
    }

    fun onRibbonItemSelected(id: String) {
        if (_personalListStateFlow.value.ribbon.find { it.id == id }?.isSelected == true) {
            return
        }
        currentRibbonId = id
        val ribbon = personalListComposer.composeStatuses(ribbonRaw, id)
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            ribbon = ribbon,
            items = listOf(),
        )
        viewModelScope.launch(context = Dispatchers.IO) {
            currentRibbonId?.let {
                fetchPage(it)
            }
        }
    }

    fun onOrderChanged(isAscending: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = !isAscending
            personalListFilterRepository.setOrder(order)

            val filteredItems = personalListComposer.applyFilter(
                items = userRatesList,
                personalListFilterEntity = personalListFilterRepository.getFilter(),
                query = query,
            )

            val filter = personalListFilterRepository.getFilter()
            val userRatesUI = filteredItems.mapNotNull(personalListComposer::convert)

            val items = personalListComposer.addFilter(userRatesUI, filter)

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = items,
                isPullDownRefreshing = false,
            )

        }
    }

    fun onSortChanged(sort: String?) {
        if (sort == null) return

        viewModelScope.launch(Dispatchers.IO) {
            personalListFilterRepository.setSort(sort)

            val filteredItems = personalListComposer.applyFilter(
                items = userRatesList,
                personalListFilterEntity = personalListFilterRepository.getFilter(),
                query = query,
            )

            val filter = personalListFilterRepository.getFilter()
            val userRatesUI = filteredItems.mapNotNull(personalListComposer::convert)

            val items = personalListComposer.addFilter(userRatesUI, filter)

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = items,
                isPullDownRefreshing = false,
            )

        }
    }

    fun onSortClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val filter = personalListFilterRepository.getFilter()
            val events = _personalListStateFlow.value.events

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                events = events + Event.NavigateToSortFilter(
                    currentSort = filter.sort.sort,
                ),
            )
        }
    }

    fun onEventConsumed(event: Event) {
        val value = _personalListStateFlow.value
        _personalListStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

        viewModelScope.launch(Dispatchers.IO) {
            val filteredItems = personalListComposer.applyFilter(
                items = userRatesList,
                personalListFilterEntity = personalListFilterRepository.getFilter(),
                query = query,
            )

            val filter = personalListFilterRepository.getFilter()
            val userRatesUI = filteredItems.mapNotNull(personalListComposer::convert)

            val items = personalListComposer.addFilter(userRatesUI, filter)

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = items,
                isPullDownRefreshing = false,
            )
        }
    }

    fun onEditRateClicked(editRateId: Long) {
        val events = _personalListStateFlow.value.events

        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            events = events + Event.NavigateToEditRate(
                rateId = editRateId,
            ),
        )
    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        if (userRate == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            userRatesRepository.updateUserRate(
                rateId = userRate.rateId,
                status = userRate.status,
                episodes = userRate.episodes,
                score = userRate.score,
            )
                .onSuccess {
                    currentRibbonId?.let { id ->
                        fetchPage(id)
                    }
                }
                .onFailure { exception ->
                    Log.d("PersonalListViewModel", exception.toString())
                }
        }
    }

    private suspend fun fetchPage(status: String) {
        userRatesRepository
            .getUserRates(status = status, page = 1)
            .collect { result ->
                result
                    .map { list ->
                        val filter = personalListFilterRepository.getFilter()
                        userRatesList = list
                        val filteredItems = personalListComposer.applyFilter(
                            items = list,
                            personalListFilterEntity = filter,
                            query = query,
                        )

                        val userRatesUI = filteredItems.mapNotNull(personalListComposer::convert)

                        personalListComposer.addFilter(userRatesUI, filter)
                    }
                    .onSuccess { items ->
                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            items = items,
                            isPullDownRefreshing = false,
                        )
                    }
                    .onFailure { exception ->
                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            isPullDownRefreshing = false,
                        )
                        Log.d("PersonalListViewModel", exception.toString())
                    }

            }
    }

}
