@file:Suppress("MagicNumber")

package com.dezdeqness.foundation.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object LightAnimeColors {
    val Primary = Color(0xFF42A5F5)
    val PrimaryVariant = Color(0xFF1E88E5)
    val Secondary = Color(0xFF64D8CB)
    val Background = Color(0xFFF5F9FF)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFEAF2FB)
    val Border = Color(0xFFD6E4F0)
    val OnPrimary = Color.White
    val OnSecondary = Color(0xFF003733)
    val OnBackground = Color(0xFF1A1C1E)
    val OnSurface = Color(0xFF2A2E33)
    val Error = Color(0xFFFF6B6B)
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFB74D)
    val TextPrimary = Color(0xFF1A1C1E)
    val TextSecondary = Color(0xFF5F6368)
    val TextDisabled = Color(0xFF9AA0A6)
    val Ripple = Color(0x14000000)
    val Accent = Color(0xFF7C8CFF)
    val White = white
    val Black = black
}

object DarkAnimeColors {
    val Primary = Color(0xFF90CAF9)
    val PrimaryVariant = Color(0xFF42A5F5)
    val Secondary = Color(0xFF4DD0C8)
    val Background = Color(0xFF121417)
    val Surface = Color(0xFF1B1F24)
    val SurfaceVariant = Color(0xFF252A31)
    val Border = Color(0xFF323843)
    val OnPrimary = Color(0xFF0F1419)
    val OnSecondary = Color(0xFF002E2A)
    val OnBackground = Color(0xFFE3EAF2)
    val OnSurface = Color(0xFFD1D9E0)
    val Error = Color(0xFFFF8A80)
    val Success = Color(0xFF81C784)
    val Warning = Color(0xFFFFCC80)
    val TextPrimary = Color(0xFFE3EAF2)
    val TextSecondary = Color(0xFFB0BEC5)
    val TextDisabled = Color(0xFF6B7680)
    val Ripple = Color(0x26FFFFFF)
    val Accent = Color(0xFF8C9EFF)
    val White = white
    val Black = black
}

object AmoledAnimeColors {
    val Primary = DarkAnimeColors.Primary
    val PrimaryVariant = DarkAnimeColors.PrimaryVariant
    val Secondary = DarkAnimeColors.Secondary
    val Background = Color(0xFF000000)
    val Surface = Color(0xFF0A0D12)
    val SurfaceVariant = Color(0xFF141922)
    val Border = Color(0xFF222833)
    val OnPrimary = Color(0xFF000000)
    val OnSecondary = DarkAnimeColors.OnSecondary
    val OnBackground = Color(0xFFE6EDF5)
    val OnSurface = Color(0xFFDDE5EE)
    val Error = DarkAnimeColors.Error
    val Success = DarkAnimeColors.Success
    val Warning = DarkAnimeColors.Warning
    val TextPrimary = Color(0xFFEAF2FB)
    val TextSecondary = Color(0xFFAAB6C2)
    val TextDisabled = Color(0xFF5F6B76)
    val Ripple = DarkAnimeColors.Ripple
    val Accent = DarkAnimeColors.Accent
    val White = white
    val Black = black
}

val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)

data class NamedColor(val name: String, val color: Color)

val lightColors = listOf(
    NamedColor("Primary", LightAnimeColors.Primary),
    NamedColor("PrimaryVariant", LightAnimeColors.PrimaryVariant),
    NamedColor("Secondary", LightAnimeColors.Secondary),
    NamedColor("Background", LightAnimeColors.Background),
    NamedColor("Surface", LightAnimeColors.Surface),
    NamedColor("SurfaceVariant", LightAnimeColors.SurfaceVariant),
    NamedColor("Border", LightAnimeColors.Border),
    NamedColor("OnPrimary", LightAnimeColors.OnPrimary),
    NamedColor("OnSecondary", LightAnimeColors.OnSecondary),
    NamedColor("OnBackground", LightAnimeColors.OnBackground),
    NamedColor("OnSurface", LightAnimeColors.OnSurface),
    NamedColor("Error", LightAnimeColors.Error),
    NamedColor("Success", LightAnimeColors.Success),
    NamedColor("Warning", LightAnimeColors.Warning),
    NamedColor("TextPrimary", LightAnimeColors.TextPrimary),
    NamedColor("TextSecondary", LightAnimeColors.TextSecondary),
    NamedColor("TextDisabled", LightAnimeColors.TextDisabled),
    NamedColor("Ripple", LightAnimeColors.Ripple),
    NamedColor("Accent", LightAnimeColors.Accent),
    NamedColor("White", LightAnimeColors.White),
    NamedColor("Black", LightAnimeColors.Black),
)

val darkColors = listOf(
    NamedColor("Primary", DarkAnimeColors.Primary),
    NamedColor("PrimaryVariant", DarkAnimeColors.PrimaryVariant),
    NamedColor("Secondary", DarkAnimeColors.Secondary),
    NamedColor("Background", DarkAnimeColors.Background),
    NamedColor("Surface", DarkAnimeColors.Surface),
    NamedColor("SurfaceVariant", DarkAnimeColors.SurfaceVariant),
    NamedColor("Border", DarkAnimeColors.Border),
    NamedColor("OnPrimary", DarkAnimeColors.OnPrimary),
    NamedColor("OnSecondary", DarkAnimeColors.OnSecondary),
    NamedColor("OnBackground", DarkAnimeColors.OnBackground),
    NamedColor("OnSurface", DarkAnimeColors.OnSurface),
    NamedColor("Error", DarkAnimeColors.Error),
    NamedColor("Success", DarkAnimeColors.Success),
    NamedColor("Warning", DarkAnimeColors.Warning),
    NamedColor("TextPrimary", DarkAnimeColors.TextPrimary),
    NamedColor("TextSecondary", DarkAnimeColors.TextSecondary),
    NamedColor("TextDisabled", DarkAnimeColors.TextDisabled),
    NamedColor("Ripple", DarkAnimeColors.Ripple),
    NamedColor("Accent", DarkAnimeColors.Accent),
    NamedColor("White", DarkAnimeColors.White),
    NamedColor("Black", DarkAnimeColors.Black),
)

val amoledColors = listOf(
    NamedColor("Primary", AmoledAnimeColors.Primary),
    NamedColor("PrimaryVariant", AmoledAnimeColors.PrimaryVariant),
    NamedColor("Secondary", AmoledAnimeColors.Secondary),
    NamedColor("Background", AmoledAnimeColors.Background),
    NamedColor("Surface", AmoledAnimeColors.Surface),
    NamedColor("SurfaceVariant", AmoledAnimeColors.SurfaceVariant),
    NamedColor("Border", AmoledAnimeColors.Border),
    NamedColor("OnPrimary", AmoledAnimeColors.OnPrimary),
    NamedColor("OnSecondary", AmoledAnimeColors.OnSecondary),
    NamedColor("OnBackground", AmoledAnimeColors.OnBackground),
    NamedColor("OnSurface", AmoledAnimeColors.OnSurface),
    NamedColor("Error", AmoledAnimeColors.Error),
    NamedColor("Success", AmoledAnimeColors.Success),
    NamedColor("Warning", AmoledAnimeColors.Warning),
    NamedColor("TextPrimary", AmoledAnimeColors.TextPrimary),
    NamedColor("TextSecondary", AmoledAnimeColors.TextSecondary),
    NamedColor("TextDisabled", AmoledAnimeColors.TextDisabled),
    NamedColor("Ripple", AmoledAnimeColors.Ripple),
    NamedColor("Accent", AmoledAnimeColors.Accent),
    NamedColor("White", AmoledAnimeColors.White),
    NamedColor("Black", AmoledAnimeColors.Black),
)

@Composable
fun ColorListPreview(title: String, colors: List<NamedColor>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = AppTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        colors.forEach { colorItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(48.dp)
                    .background(colorItem.color),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = colorItem.name,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    color = if (colorItem.color.luminance() < 0.5f) Color.White else Color.Black
                )
                Text(
                    text = "#${colorItem.color.value.toString(16).uppercase().take(8)}",
                    modifier = Modifier.padding(end = 16.dp),
                    color = if (colorItem.color.luminance() < 0.5f) Color.White else Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1250)
@Composable
fun LightAnimeColorsPreview() {
    AppTheme {
        ColorListPreview("Light Anime Colors", lightColors)
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    heightDp = 1250
)
@Composable
fun DarkAnimeColorsPreview() {
    AppTheme {
        ColorListPreview("Dark Anime Colors", darkColors)
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    heightDp = 1250
)
@Composable
fun AmoledAnimeColorsPreview() {
    AppTheme {
        ColorListPreview("Amoled Anime Colors", amoledColors)
    }
}
