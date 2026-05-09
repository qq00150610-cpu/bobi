package com.example.bobi.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DeepPurple = Color(0xFF1A1A2E)
val ElectricViolet = Color(0xFF7B2FF7)
val NeonCyan = Color(0xFF00D4FF)
val SoftLavender = Color(0xFFB19CD9)
val DarkSurface = Color(0xFF16213E)
val DarkCard = Color(0xFF0F3460)

private val DarkColorScheme = darkColorScheme(
    primary = ElectricViolet,
    secondary = NeonCyan,
    tertiary = SoftLavender,
    background = DeepPurple,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun BobiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
