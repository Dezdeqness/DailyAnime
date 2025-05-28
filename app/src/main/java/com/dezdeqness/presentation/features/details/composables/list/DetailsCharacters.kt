package com.dezdeqness.presentation.features.details.composables.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.presentation.models.RoleUiModel


@Composable
fun DetailsCharacters(
    modifier: Modifier = Modifier,
    characters: List<RoleUiModel>,
    onCharacterClick: (Long) -> Unit
) {
    Column(modifier = modifier) {
        Header(
            title = stringResource(R.string.anime_details_characters_title),
            titleStyle = AppTheme.typography.headlineSmall,
            verticalPadding = 8.dp,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(characters.size) { index ->
                val character = characters[index]
                CharacterItem(
                    character = character,
                    onCharacterClick = onCharacterClick,
                    )
            }
        }
    }
}

@Composable
private fun CharacterItem(
    modifier: Modifier = Modifier,
    character: RoleUiModel,
    onCharacterClick: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .width(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { onCharacterClick(character.id) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
    ) {
        AppImage(
            data = character.imageUrl,
            modifier = Modifier.size(80.dp),
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
