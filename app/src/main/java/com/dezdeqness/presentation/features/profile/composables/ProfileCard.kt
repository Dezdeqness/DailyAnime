package com.dezdeqness.presentation.features.profile.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun ProfileCard(
    nickname: String,
    avatar: String,
    onHistoryClicked: () -> Unit,
    onStatsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.onPrimary)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val model = remember {
            ImageRequest.Builder(context)
                .data(avatar)
                .crossfade(true)
                .build()
        }

        AsyncImage(
            model = model,
            contentDescription = nickname,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .width(80.dp)
                .height(80.dp)
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
            onLogoutClicked = {},
        )
    }
}
