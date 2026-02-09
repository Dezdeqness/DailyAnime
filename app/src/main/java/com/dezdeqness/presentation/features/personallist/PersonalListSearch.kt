package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.feature.personallist.search.PersonalListSearchActions
import com.dezdeqness.feature.personallist.search.PersonalListSearchExpandedPage
import com.dezdeqness.feature.personallist.search.PersonalListSearchViewModel
import com.dezdeqness.feature.personallist.search.PersonalListTab
import com.dezdeqness.feature.personallist.search.composables.PersonalSearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalListSearch(
    modifier: Modifier = Modifier,
    scrollBehavior: SearchBarScrollBehavior,
    onDetailsClick: (animeId: Long, title: String) -> Unit,
) {
    val context = LocalContext.current
    val personalListSearchComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .personalListSearchComponent()
            .create()
    }
    val viewModelFactory = personalListSearchComponent.viewModelFactory()
    val viewModel = viewModel<PersonalListSearchViewModel>(factory = viewModelFactory)

    val searchBarState = rememberSearchBarState()

    val textFieldState = rememberTextFieldState()

    val actions = remember {
        object : PersonalListSearchActions {
            override fun onTabSelect(tab: PersonalListTab) {
                viewModel.onTabSelected(tab)
            }

            override fun onAnimeClick(animeId: Long, title: String) {
                onDetailsClick(animeId, title)
            }

        }
    }

    val inputField = @Composable {
        PersonalSearchTextField(
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = {
                viewModel.onQueryChanged(it)
            },
            modifier = modifier,
        )
    }

    TopSearchBar(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        inputField = inputField,
        colors = SearchBarDefaults.colors(
            containerColor = AppTheme.colors.onPrimary,
        )
    )
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
    ) {
        PersonalListSearchExpandedPage(
            stateFlow = viewModel.searchState,
            modifier = modifier,
            actions = actions,
        )
    }
}
