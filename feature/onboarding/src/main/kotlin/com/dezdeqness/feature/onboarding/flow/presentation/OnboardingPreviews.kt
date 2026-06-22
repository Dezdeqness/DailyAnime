package com.dezdeqness.feature.onboarding.flow.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.onboarding.flow.presentation.composables.DoneStep
import com.dezdeqness.feature.onboarding.flow.presentation.composables.GenresStep
import com.dezdeqness.feature.onboarding.flow.presentation.composables.NotificationsStep
import com.dezdeqness.feature.onboarding.flow.presentation.composables.WelcomeStep
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme

private val previewGenres = listOf(
    GenreUiModel(id = "1", name = "Экшен", isGenre = true),
    GenreUiModel(id = "2", name = "Комедия", isGenre = true),
    GenreUiModel(id = "3", name = "Драма", isGenre = true),
    GenreUiModel(id = "4", name = "Фэнтези", isGenre = true),
    GenreUiModel(id = "5", name = "Романтика", isGenre = true),
    GenreUiModel(id = "6", name = "Школа", isGenre = false),
    GenreUiModel(id = "7", name = "Психология", isGenre = false),
    GenreUiModel(id = "8", name = "Музыка", isGenre = false),
)

private fun previewModifier(height: Int) = Modifier
    .fillMaxWidth()
    .height(height.dp)

@PreviewLightDark
@Composable
fun WelcomeStepPreview() {
    AppTheme {
        WelcomeStep(
            animateOnLaunch = false,
            modifier = previewModifier(640).background(AppTheme.colors.onPrimary),
        )
    }
}

@PreviewLightDark
@Composable
fun GenresStepPreview() {
    AppTheme {
        GenresStep(
            genres = previewGenres,
            selectedIds = setOf("1", "3", "5"),
            minSelection = 3,
            onGenreClick = {},
            modifier = previewModifier(640).background(AppTheme.colors.onPrimary),
        )
    }
}

@PreviewLightDark
@Composable
fun NotificationsStepPreview() {
    AppTheme {
        NotificationsStep(
            notificationsEnabled = true,
            hours = 17,
            minutes = 0,
            onToggle = {},
            onTimeChanged = { _, _ -> },
            modifier = previewModifier(640).background(AppTheme.colors.onPrimary),
        )
    }
}

@PreviewLightDark
@Composable
fun DoneStepPreview() {
    AppTheme {
        DoneStep(
            selectedGenreNames = listOf("Экшен", "Драма", "Детектив"),
            notificationsEnabled = true,
            notificationTimeLabel = "17:00",
            modifier = previewModifier(640).background(AppTheme.colors.onPrimary),
        )
    }
}
