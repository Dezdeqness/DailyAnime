package com.dezdeqness.feature.onboarding.selectgenres.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.feature.onboarding.R
import com.dezdeqness.feature.onboarding.selectgenres.presentation.composables.SelectGenresContent
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGenresContentPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<SelectGenresUiState>,
    actions: SelectGenresActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                navigationIcon = Icons.Filled.Close,
                navigationClick = actions::onBackPressed,
            )
        },
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            SelectGenresContent(
                modifier = modifier.fillMaxSize(),
                list = state.genres,
                selectedIds = state.selectedGenres,
                onGenreClick = actions::onGenreClicked,
            )

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = state.selectedGenres.size >= 3,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                ) + fadeOut()
            ) {
                AppButton(
                    title = stringResource(R.string.onboarding_select_genres_save),
                    onClick = { actions.onSaveClicked() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun SelectGenresContentPageNonSavePreview() {
    AppTheme {
        val stateFlow = MutableStateFlow(
            SelectGenresUiState(
                genres = listOf(
                    GenreUiModel(id = "1", name = "Genre 1", isGenre = true),
                    GenreUiModel(id = "2", name = "Genre 2", isGenre = true),
                    GenreUiModel(id = "3", name = "Theme 1", isGenre = false),
                    GenreUiModel(id = "4", name = "Genre 2", isGenre = false),
                    GenreUiModel(id = "5", name = "Genre 3", isGenre = false),
                ),
                selectedGenres = setOf("1", "3"),
            ),
        )

        SelectGenresContentPage(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            stateFlow = stateFlow,
            actions = object : SelectGenresActions {
                override fun onBackPressed() {
                }

                override fun onGenreClicked(genreId: String) {
                }

                override fun onSaveClicked() {
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
fun SelectGenresContentPagePreview() {
    AppTheme {
        val stateFlow = MutableStateFlow(
            SelectGenresUiState(
                genres = listOf(
                    GenreUiModel(id = "1", name = "Genre 1", isGenre = true),
                    GenreUiModel(id = "2", name = "Genre 2", isGenre = true),
                    GenreUiModel(id = "3", name = "Theme 1", isGenre = false),
                    GenreUiModel(id = "4", name = "Genre 2", isGenre = false),
                    GenreUiModel(id = "5", name = "Genre 3", isGenre = false),
                ),
                selectedGenres = setOf("1", "2", "3"),
            ),
        )

        SelectGenresContentPage(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            stateFlow = stateFlow,
            actions = object : SelectGenresActions {
                override fun onBackPressed() {
                }

                override fun onGenreClicked(genreId: String) {
                }

                override fun onSaveClicked() {
                }
            }
        )
    }
}
