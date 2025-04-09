package com.dezdeqness.presentation.features.animedetails.composables.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.RoleUiModel


@Composable
fun DetailsCharacters(
    modifier: Modifier = Modifier,
    characters: List<RoleUiModel>,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(R.string.anime_details_characters_title),
            style = AppTheme.typography.headlineSmall,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(characters.size) { index ->
                val character = characters[index]
                CharacterItem(character = character)
            }
        }
    }
}

@Composable
private fun CharacterItem(
    modifier: Modifier = Modifier,
    character: RoleUiModel,
) {
    val context = LocalContext.current

    Column(modifier = modifier.width(80.dp)) {
        val model = remember {
            ImageRequest.Builder(context)
                .data(character.imageUrl)
                .crossfade(true)
                .build()
        }

        AsyncImage(
            model = model,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            error = painterResource(id = R.drawable.ic_placeholder),
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
        )

        Box(
            modifier = Modifier.padding(bottom = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                character.name,
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "",
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
        }
    }

}
