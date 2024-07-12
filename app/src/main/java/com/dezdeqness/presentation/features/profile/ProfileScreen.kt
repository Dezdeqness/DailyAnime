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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core_ui.theme.AppTheme
import com.dezdeqness.presentation.features.profile.composables.ProfileCard
import com.dezdeqness.presentation.features.profile.composables.UnauthorizedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    actions: ProfileActions,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = actions::onSettingIconClicked,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile_label_gear),
                            tint = AppTheme.colors.onSecondary,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = AppTheme.colors.onPrimary,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(AppTheme.colors.onPrimary)
        ) {
            val isAuthorized = state.isAuthorized

            if (isAuthorized) {
                ProfileCard(
                    nickname = state.nickname,
                    avatar = state.avatar,
                    onStatsClicked = {
                        actions.onStatsIconClicked()
                    },
                    onHistoryClicked = {
                        actions.onHistoryIconClicked()
                    },
                    onLogoutClicked = {
                        actions.onLogoutClicked()
                    }
                )
            } else {
                UnauthorizedCard(
                    onLoginClick = actions::onLoginCLicked,
                    onRegisterClick = actions::onRegistrationClicked,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
            state = ProfileState(),
            actions = object : ProfileActions {
                override fun onSettingIconClicked() = Unit
                override fun onStatsIconClicked() = Unit
                override fun onHistoryIconClicked() = Unit
                override fun onLoginCLicked() = Unit
                override fun onRegistrationClicked() = Unit
                override fun onLogoutClicked() = Unit
            }
        )
    }
}
