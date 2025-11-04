package com.dezdeqness.presentation.features.profile.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.image.AppImage

@Composable
fun ProfileCard(
    nickname: String,
    avatar: String,
    onHistoryClicked: () -> Unit,
    onAchievementsClicked: () -> Unit,
    onStatsClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.onPrimary)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppImage(
            data = avatar,
            contentScale = ContentScale.Fit,
            contentDescription = nickname,
            modifier = Modifier.size(80.dp),
        )
        Text(
            nickname,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp),
            color = AppTheme.colors.textPrimary
        )

        ProfileButton(
            title = stringResource(id = R.string.profile_statistics),
            icon = R.drawable.ic_profile_label_statistics,
            onClick = onStatsClicked,
        )
        ProfileButton(
            title = stringResource(id = R.string.profile_achievements),
            icon = R.drawable.ic_profile_label_achievements,
            onClick = onAchievementsClicked,
        )
        ProfileButton(
            title = stringResource(id = R.string.profile_favourites),
            icon = R.drawable.ic_profile_label_favouritte,
            onClick = onFavouriteClicked,
        )
        ProfileButton(
            title = stringResource(id = R.string.profile_history),
            icon = R.drawable.ic_profile_label_history,
            onClick = onHistoryClicked,
        )
        ProfileButton(
            title = stringResource(id = R.string.profile_logout),
            icon = R.drawable.ic_logout,
            onClick = onLogoutClicked,
        )
    }

}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileCardPreview() {
    AppTheme {
        ProfileCard(
            nickname = "Staria",
            "no_url",
            onHistoryClicked = {},
            onStatsClicked = {},
            onAchievementsClicked = {},
            onFavouriteClicked = {},
            onLogoutClicked = {},
        )
    }
}
