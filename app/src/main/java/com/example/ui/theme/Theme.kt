package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimary,
    onPrimary = AmberOnPrimary,
    primaryContainer = AmberOnContainer,
    onPrimaryContainer = AmberContainer,
    secondary = TerracottaSecondary,
    onSecondary = AmberOnPrimary,
    secondaryContainer = TerracottaOnContainer,
    onSecondaryContainer = TerracottaContainer,
    tertiary = FreshGreen,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = AmberPrimary,
    onPrimary = AmberOnPrimary,
    primaryContainer = AmberContainer,
    onPrimaryContainer = AmberOnContainer,
    secondary = TerracottaSecondary,
    onSecondary = AmberOnPrimary,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = TerracottaOnContainer,
    tertiary = FreshGreen,
    background = CreamBackground,
    surface = CardSurface,
    surfaceVariant = SurfaceVariantWarm,
    onBackground = OnSurfaceWarm,
    onSurface = OnSurfaceWarm,
    onSurfaceVariant = OnSurfaceSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our intentional food palette for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
