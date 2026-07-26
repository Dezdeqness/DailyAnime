package com.dezdeqness.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.profile.presentation.composables.ProfileCard
import com.dezdeqness.feature.profile.presentation.composables.ProfileSkeleton
import com.dezdeqness.feature.profile.presentation.composables.UnauthorizedCard
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import com.dezdeqness.shared.presentation.model.AuthorizedUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    stateFlow: StateFlow<ProfileState>,
    actions: ProfileActions,
    modifier: Modifier = Modifier,
) {
    val state by stateFlow.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                navigationIcon = null,
                actions = {
                    IconButton(onClick = actions::onSettingIconClicked) {
                        Icon(
                            Icons.Outlined.Settings,
                            tint = AppTheme.colors.onSurface,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize()
                .background(AppTheme.colors.onPrimary),
        ) {
            when (state.authorizedState) {
                AuthorizedUiState.Authorized -> {
                    ProfileCard(
                        nickname = state.nickname,
                        avatar = state.avatar,
                        onStatsClicked = actions::onStatsIconClicked,
                        onHistoryClicked = actions::onHistoryIconClicked,
                        onAchievementsClicked = {
                            state.userId?.let { actions.onAchievementsClicked(it) }
                        },
                        onFavouriteClicked = {
                            state.userId?.let { actions.onFavouriteClicked(it) }
                        },
                        onLogoutClicked = actions::onLogoutClicked,
                    )
                }

                AuthorizedUiState.Unauthorized -> {
                    UnauthorizedCard(
                        onLoginClick = actions::onLoginClicked,
                        onRegisterClick = actions::onRegistrationClicked,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                AuthorizedUiState.Pending -> {
                    ProfileSkeleton()
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun ProfilePagePreview() {
    AppTheme {
        ProfilePage(
            stateFlow = MutableStateFlow(
                ProfileState(
                    authorizedState = AuthorizedUiState.Authorized,
                    nickname = "Staria",
                ),
            ),
            actions = PreviewProfileActions,
        )
    }
}

@PreviewLightDark
@Composable
fun ProfilePageUnauthorizedPreview() {
    AppTheme {
        ProfilePage(
            stateFlow = MutableStateFlow(ProfileState(authorizedState = AuthorizedUiState.Unauthorized)),
            actions = PreviewProfileActions,
        )
    }
}

private val PreviewProfileActions = object : ProfileActions {
    override fun onSettingIconClicked() = Unit
    override fun onStatsIconClicked() = Unit
    override fun onHistoryIconClicked() = Unit
    override fun onAchievementsClicked(userId: Long) = Unit
    override fun onLoginClicked() = Unit
    override fun onRegistrationClicked() = Unit
    override fun onFavouriteClicked(userId: Long) = Unit
    override fun onLogoutClicked() = Unit
}
