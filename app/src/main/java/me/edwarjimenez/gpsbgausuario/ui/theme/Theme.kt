package me.edwarjimenez.gpsbgausuario.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GreenPrimary = Color(0xFF2E7D32)
val GreenAccent = Color(0xFF4CAF50)
val GreenLight = Color(0xFF81C784)
val GreenDark = Color(0xFF1B5E20)
val YellowAccent = Color(0xFFFFD700)
val BgDark = Color(0xFF0A1A0A)
val BgCard = Color(0xFF0F2A0F)
val TextPrimary = Color(0xFFE8F5E9)
val TextSecondary = Color(0xFFA5D6A7)
val TextMuted = Color(0xFF558B2F)
val RedError = Color(0xFFFF3B57)

private val ColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenAccent,
    tertiary = YellowAccent,
    background = BgDark,
    surface = BgCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun GpsBGAUsuarioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        content = content
    )
}