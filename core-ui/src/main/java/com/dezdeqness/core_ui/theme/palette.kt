package com.dezdeqness.core_ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val red50 = Color(0xFFFFEBEE)
val red100 = Color(0xFFFFCDD2)
val red200 = Color(0xFFEF9A9A)
val red300 = Color(0xFFE57373)
val red400 = Color(0xFFEF5350)
val red500 = Color(0xFFF44336)
val red600 = Color(0xFFE53935)
val red700 = Color(0xFFD32F2F)
val red800 = Color(0xFFC62828)
val red900 = Color(0xFFB71C1C)
val redA100 = Color(0xFFFF8A80)
val redA200 = Color(0xFFFF5252)
val redA400 = Color(0xFFFF1744)
val redA700 = Color(0xFFD50000)

val purple50 = Color(0xFFF3E5F5)
val purple100 = Color(0xFFE1BEE7)
val purple200 = Color(0xFFCE93D8)
val purple300 = Color(0xFFBA68C8)
val purple400 = Color(0xFFAB47BC)
val purple500 = Color(0xFF9C27B0)
val purple600 = Color(0xFF8E24AA)
val purple700 = Color(0xFF7B1FA2)
val purple800 = Color(0xFF6A1B9A)
val purple900 = Color(0xFF4A148C)
val purpleA100 = Color(0xFFEA80FC)
val purpleA200 = Color(0xFFE040FB)
val purpleA400 = Color(0xFFD500F9)
val purpleA700 = Color(0xFFAA00FF)

val deepPurple50 = Color(0xFFEDE7F6)
val deepPurple100 = Color(0xFFD1C4E9)
val deepPurple200 = Color(0xFFB39DDB)
val deepPurple300 = Color(0xFF9575CD)
val deepPurple400 = Color(0xFF7E57C2)
val deepPurple500 = Color(0xFF673AB7)
val deepPurple600 = Color(0xFF5E35B1)
val deepPurple700 = Color(0xFF512DA8)
val deepPurple800 = Color(0xFF4527A0)
val deepPurple900 = Color(0xFF311B92)
val deepPurpleA100 = Color(0xFFB388FF)
val deepPurpleA200 = Color(0xFF7C4DFF)
val deepPurpleA400 = Color(0xFF651FFF)
val deepPurpleA700 = Color(0xFF6200EA)

val blue50 = Color(0xFFE3F2FD)
val blue100 = Color(0xFFBBDEFB)
val blue200 = Color(0xFF90CAF9)
val blue300 = Color(0xFF64B5F6)
val blue400 = Color(0xFF42A5F5)
val blue500 = Color(0xFF2196F3)
val blue600 = Color(0xFF1E88E5)
val blue700 = Color(0xFF1976D2)
val blue800 = Color(0xFF1565C0)
val blue900 = Color(0xFF0D47A1)
val blueA100 = Color(0xFF82B1FF)
val blueA200 = Color(0xFF448AFF)
val blueA400 = Color(0xFF2979FF)
val blueA700 = Color(0xFF2962FF)

val green50 = Color(0xFFE8F5E9)
val green100 = Color(0xFFC8E6C9)
val green200 = Color(0xFFA5D6A7)
val green300 = Color(0xFF81C784)
val green400 = Color(0xFF66BB6A)
val green500 = Color(0xFF4CAF50)
val green600 = Color(0xFF43A047)
val green700 = Color(0xFF388E3C)
val green800 = Color(0xFF2E7D32)
val green900 = Color(0xFF1B5E20)
val greenA100 = Color(0xFFB9F6CA)
val greenA200 = Color(0xFF69F0AE)
val greenA400 = Color(0xFF00E676)
val greenA700 = Color(0xFF00C853)

val yellow50 = Color(0xFFFFFDE7)
val yellow100 = Color(0xFFFFF9C4)
val yellow200 = Color(0xFFFFF59D)
val yellow300 = Color(0xFFFFF176)
val yellow400 = Color(0xFFFFEE58)
val yellow500 = Color(0xFFFFEB3B)
val yellow600 = Color(0xFFFDD835)
val yellow700 = Color(0xFFFBC02D)
val yellow800 = Color(0xFFF9A825)
val yellow900 = Color(0xFFF57F17)
val yellowA100 = Color(0xFFFFFF8D)
val yellowA200 = Color(0xFFFFFF00)
val yellowA400 = Color(0xFFFFEA00)
val yellowA700 = Color(0xFFFFD600)

val orange50 = Color(0xFFFFF3E0)
val orange100 = Color(0xFFFFE0B2)
val orange200 = Color(0xFFFFCC80)
val orange300 = Color(0xFFFFB74D)
val orange400 = Color(0xFFFFA726)
val orange500 = Color(0xFFFF9800)
val orange600 = Color(0xFFFB8C00)
val orange700 = Color(0xFFF57C00)
val orange800 = Color(0xFFEF6C00)
val orange900 = Color(0xFFE65100)
val orangeA100 = Color(0xFFFFD180)
val orangeA200 = Color(0xFFFFAB40)
val orangeA400 = Color(0xFFFF9100)
val orangeA700 = Color(0xFFFF6D00)

val gray50 = Color(0xFFFAFAFA)
val gray100 = Color(0xFFF5F5F5)
val gray200 = Color(0xFFEEEEEE)
val gray300 = Color(0xFFE0E0E0)
val gray400 = Color(0xFFBDBDBD)
val gray500 = Color(0xFF9E9E9E)
val gray600 = Color(0xFF757575)
val gray700 = Color(0xFF616161)
val gray800 = Color(0xFF424242)
val gray900 = Color(0xFF212121)

val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)

private val redPaletteList = listOf(
    red50,
    red100,
    red200,
    red300,
    red400,
    red500,
    red600,
    red700,
    red800,
    red900,
    redA100,
    redA200,
    redA400,
    redA700,
)

private val purplePaletteList = listOf(
    purple50,
    purple100,
    purple200,
    purple300,
    purple400,
    purple500,
    purple600,
    purple700,
    purple800,
    purple900,
    purpleA100,
    purpleA200,
    purpleA400,
    purpleA700,
)

private val deepPurplePaletteList = listOf(
    deepPurple50,
    deepPurple100,
    deepPurple200,
    deepPurple300,
    deepPurple400,
    deepPurple500,
    deepPurple600,
    deepPurple700,
    deepPurple800,
    deepPurple900,
    deepPurpleA100,
    deepPurpleA200,
    deepPurpleA400,
    deepPurpleA700,
)

private val bluePaletteList = listOf(
    blue50,
    blue100,
    blue200,
    blue300,
    blue400,
    blue500,
    blue600,
    blue700,
    blue800,
    blue900,
    blueA100,
    blueA200,
    blueA400,
    blueA700,
)

private val greenPaletteList = listOf(
    green50,
    green100,
    green200,
    green300,
    green400,
    green500,
    green600,
    green700,
    green800,
    green900,
    greenA100,
    greenA200,
    greenA400,
    greenA700,
)

private val yellowPaletteList = listOf(
    yellow50,
    yellow100,
    yellow200,
    yellow300,
    yellow400,
    yellow500,
    yellow600,
    yellow700,
    yellow800,
    yellow900,
    yellowA100,
    yellowA200,
    yellowA400,
    yellowA700,
)

private val orangePaletteList = listOf(
    orange50,
    orange100,
    orange200,
    orange300,
    orange400,
    orange500,
    orange600,
    orange700,
    orange800,
    orange900,
    orangeA100,
    orangeA200,
    orangeA400,
    orangeA700,
)

private val grayPaletteList = listOf(
    gray50,
    gray100,
    gray200,
    gray300,
    gray400,
    gray500,
    gray600,
    gray700,
    gray800,
    gray900,
)

@Composable
private fun ColorsList(colors: List<Color>) {
    Column {
        colors.forEach {
            ColorBox(it)
        }
    }
}

@Composable
private fun ColorBox(color: Color) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .height(40.dp)
            .width(100.dp)
            .background(color = color, RoundedCornerShape(4.dp))
    )
}

@Preview(name = "Red palette")
@Composable
fun previewRedPalette() {
    ColorsList(colors = redPaletteList)
}

@Preview(name = "Purple palette")
@Composable
fun previewPurplePalette() {
    ColorsList(colors = purplePaletteList)
}

@Preview(name = "Deep purple palette")
@Composable
fun previewDeepPurplePalette() {
    ColorsList(colors = deepPurplePaletteList)
}

@Preview(name = "Blue palette")
@Composable
fun previewBluePalette() {
    ColorsList(colors = bluePaletteList)
}

@Preview(name = "Green palette")
@Composable
fun previewGreenPalette() {
    ColorsList(colors = greenPaletteList)
}

@Preview(name = "Yellow palette")
@Composable
fun previewYellowPalette() {
    ColorsList(colors = yellowPaletteList)
}

@Preview(name = "Orange palette")
@Composable
fun previewOrangePalette() {
    ColorsList(colors = orangePaletteList)
}

@Preview(name = "Gray palette")
@Composable
fun previewGrayPalette() {
    ColorsList(colors = grayPaletteList)
}

@Preview(name = "Black&White palette")
@Composable
fun previewBlackAndWhitePalette() {
    ColorsList(
        colors = listOf(
            black,
            white,
        ),
    )
}