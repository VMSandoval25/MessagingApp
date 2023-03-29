package app.getsizzle.messaging.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf


/**
 * @author Ryan Simon
 **/

private val LocalAppColors = staticCompositionLocalOf {
    defaultColors
}

private val LocalAppTypography = staticCompositionLocalOf {
    defaultTypography
}

object SizzleTheme {
    val colors: SizzleColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: SizzleTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}

/**
 * Some setup inspired by https://blog.xmartlabs.com/blog/extending-material-theme-in-jetpack-compose/
 */
@Composable
fun SizzleTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppColors provides sizzleColors(darkTheme = darkTheme),
        LocalAppTypography provides sizzleTypography()
    ) {
        val localColors = LocalAppColors.current
        val localTypography = LocalAppTypography.current
        MaterialTheme(
            content = content,
            colorScheme = lightColorScheme(
                primary = localColors.primary,
                onPrimary = localColors.onPrimary,
                //primaryContainer = localColors.primary,
                //onPrimaryContainer = localColors.primaryVariant,
                //inversePrimary =localColors.primaryVariantTwo ,
                secondary = localColors.secondary,
                onSecondary = localColors.onSecondary,
                //secondaryContainer = localColors.secondaryVariant,
                //onSecondaryContainer = localColors.secondaryVariantTwo,
                //tertiary = localColors.accent,
                //onTertiary = localColors.accentVariant,
                //tertiaryContainer = localColors.accentThreeVariant,
                //onTertiaryContainer = localColors.accentVariantTwo,
                error = localColors.error,
                onError = localColors.onError,
                //errorContainer = localColors.error,
                //onErrorContainer = localColors.onError,
                background = localColors.background,
                onBackground = localColors.onBackground,
                surface = localColors.surface,
                onSurface = localColors.onSurface,
                //inverseSurface = localColors.surface,
                //inverseOnSurface = localColors.onSurface,
                //surfaceVariant = localColors.onBackgroundSurface,
                //onSurfaceVariant = localColors.onBackgroundVariant,
                //outline = localColors.primaryVariant,
                //scrim=localColors.primary,
                //surfaceTint = localColors.accentThreeVariant,
                //outlineVariant = localColors.primaryVariant
            ),
            typography = Typography(
                displayLarge= localTypography.displayLarge,
                displayMedium = localTypography.displayMedium,
                displaySmall = localTypography.displaySmall,
                headlineLarge = localTypography.headlineLarge,
                headlineMedium = localTypography.headlineMedium,
                headlineSmall = localTypography.headlineSmall,
                titleLarge = localTypography.titleLarge,
                titleMedium = localTypography.titleMedium,
                titleSmall = localTypography.titleSmall,
                bodyLarge = localTypography.bodyLarge,
                bodyMedium = localTypography.bodyMedium,
                bodySmall=localTypography.bodySmall,
                labelMedium = localTypography.labelMedium,
                labelLarge = localTypography.labelLarge,
                labelSmall = localTypography.labelSmall
            )
        )
    }
}