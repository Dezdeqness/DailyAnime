package com.dezdeqness.presentation.features.home.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.home.AuthorizedState

@Composable
fun HomeBanner(
    modifier: Modifier = Modifier,
    authorizedState: AuthorizedState,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val model = remember(authorizedState.avatarUrl) {
            ImageRequest.Builder(context)
                .data(authorizedState.avatarUrl)
                .crossfade(true)
                .build()
        }

        AsyncImage(
            model = model,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(10))
                .size(60.dp)
        )
        val isAuthorized = authorizedState.isAuthorized

        val userName = authorizedState.userName

        val text = if (isAuthorized) {
            stringResource(R.string.home_title_authorized, userName)
        } else {
            stringResource(R.string.home_title, userName)
        }
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            style = AppTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.W500,
            ),
            color = AppTheme.colors.textPrimary,
        )
    }

}
