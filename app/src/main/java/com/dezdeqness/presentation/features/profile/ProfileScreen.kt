package com.dezdeqness.presentation.features.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.features.profile.composables.ProfileCard
import com.dezdeqness.presentation.features.profile.composables.ProfileSkeleton
import com.dezdeqness.presentation.features.profile.composables.UnauthorizedCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
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
                            painter = painterResource(id = R.drawable.ic_profile_label_gear),
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
                .background(AppTheme.colors.onPrimary)
        ) {
            when(state.authorizedState) {
                AuthorizedUiState.Authorized -> {
                    ProfileCard(
                        nickname = state.nickname,
                        avatar = state.avatar,
                        onStatsClicked = {
                            actions.onStatsIconClicked()
                        },
                        onHistoryClicked = {
                            actions.onHistoryIconClicked()
                        },
                        onAchievementsClicked = {
                            state.userId?.let { actions.onAchievementsClicked(it) }
                        },
                        onLogoutClicked = {
                            actions.onLogoutClicked()
                        }
                    )
                }
                AuthorizedUiState.Unauthorized -> {
                    UnauthorizedCard(
                        onLoginClick = actions::onLoginCLicked,
                        onRegisterClick = actions::onRegistrationClicked,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                AuthorizedUiState.Pending -> {
                    ProfileSkeleton()
                }
            }
        }
    }

}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            stateFlow = MutableStateFlow(ProfileState()),
            actions = object : ProfileActions {
                override fun onSettingIconClicked() = Unit
                override fun onStatsIconClicked() = Unit
                override fun onHistoryIconClicked() = Unit
                override fun onAchievementsClicked(userId: Long) = Unit
                override fun onLoginCLicked() = Unit
                override fun onRegistrationClicked() = Unit
                override fun onLogoutClicked() = Unit
            }
        )
    }
}
