package app.getsizzle.messaging.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * @author Ryan Simon
 **/

// Light colors
private val primaryLight = Color(0xFFE35415)
private val primaryLightVariant = Color(0xFFff8047)
private val primaryLightVariantTwo = Color(0xFFFFC5AB)
private val primaryLightVariantThree = Color(0xFFFFEEE6)
private val primaryLightText = Color(0xFF281F1E)
private val secondaryLight = Color(0xFF388f5B)
private val secondaryLightVariant = Color(0xFF4DBE76)
private val secondaryLightVariantTwo = Color(0xFFB6DE97)
private val secondaryLightVariantThree = Color(0xFFE9F5E0)
private val secondaryLightText = Color(0xFF669F8D)
private val accentLight = Color(0xFF2492C5)
private val accentLightVariant = Color(0xFF89C9E7)
private val accentLightVariantTwo = Color(0xFFb9E6F9)
private val accentTwoLight = Color(0xFFFACF5D)
private val accentThreeLight = Color(0xFFF077AD)
private val accentThreeLightVariant = Color(0xFFD84D90)
private val primaryLightBackground = Color(0xFFFFFFFF)
private val primaryLightSurface = Color(0xFFFFFFFF)
private val primaryLightOnBackgroundSurface = Color(0xFF211C1B)
private val primaryLightOnBackgroundVariant = Color(0xFF7A7776)
private val primaryLightOnBackgroundVariantTwo = Color(0xFFE9E8E8)
private val primaryLightBottomNav = Color(0xE5FFFFFF)


// Dark colors
private val primaryDark = primaryLight
private val primaryDarkVariant = primaryLightVariant
private val primaryDarkVariantTwo = primaryLightVariantTwo
private val primaryDarkVariantThree = primaryLightVariantThree
private val primaryDarkText = Color(0xFFF7F2EB)
private val secondaryDark = secondaryLight
private val secondaryDarkVariant = secondaryLightVariant
private val secondaryDarkVariantTwo = secondaryLightVariantTwo
private val secondaryDarkVariantThree = secondaryLightVariantThree
private val secondaryDarkText = Color(0xFFF7F2EB)
private val accentDark = accentLight
private val accentDarkVariant = accentLightVariant
private val accentDarkVariantTwo = accentLightVariantTwo
private val accentTwoDark = accentTwoLight
private val accentThreeDark = accentThreeLight
private val accentThreeDarkVariant = accentThreeLightVariant
private val primaryDarkBackground = Color(0xFF121212)
private val primaryDarkSurface = Color(0xFF222121)
private val primaryDarkOnBackgroundSurface = Color(0xFFFFFFFF)
private val primaryDarkOnBackgroundVariant = Color(0xFFCECECE)
private val primaryDarkOnBackgroundVariantTwo = primaryLightOnBackgroundVariantTwo

private val primaryDarkBottomNav = Color(0xE50A0909)
private val transparent = Color(0x03000000)

// Color themes
private val sizzleLightColors = SizzleColors(
    primary = primaryLight,
    primaryVariant = primaryLightVariant,
    primaryVariantTwo = primaryLightVariantTwo,
    secondary = secondaryLight,
    secondaryVariant = secondaryLightVariant,
    secondaryVariantTwo = secondaryLightVariantTwo,
    surface = primaryLightSurface,
    background = primaryLightBackground,
    error = Color.Red,
    onPrimary = primaryLightText,
    onSecondary = secondaryLightText,
    onSurface = primaryLightText,
    onBackground = primaryLightText,
    onError = primaryLightText,
    accent = accentLight,
    accentVariant = accentLightVariant,
    onBackgroundSurface = primaryLightOnBackgroundSurface,
    onBackgroundVariant = primaryLightOnBackgroundVariant,
    onBackgroundVariantTwo = primaryLightOnBackgroundVariantTwo,
    bottomNav = primaryLightBottomNav,
    transparent = transparent,
    primaryVariantThree = primaryLightVariantThree,
    secondaryVariantThree = secondaryLightVariantThree,
    accentVariantTwo = accentLightVariantTwo,
    accentVariantThree = accentLightVariant,
    accentTwo = accentTwoLight,
    accentThree = accentThreeLight,
    accentThreeVariant = accentThreeLightVariant)

private val sizzleDarkColors = SizzleColors(
    primary = primaryDark,
    primaryVariant = primaryDarkVariant,
    primaryVariantTwo = primaryDarkVariantTwo,
    secondary = secondaryDark,
    secondaryVariant = secondaryDarkVariant,
    secondaryVariantTwo = secondaryDarkVariantTwo,
    surface = primaryDarkSurface,
    background = primaryDarkBackground,
    error = Color.Red,
    accent = accentDark,
    accentVariant = accentDarkVariant,
    onPrimary = primaryDarkText,
    onSecondary = secondaryDarkText,
    onSurface = primaryDarkText,
    onBackground = primaryDarkText,
    onError = primaryDarkText,
    onBackgroundSurface = primaryDarkOnBackgroundSurface,
    onBackgroundVariant = primaryDarkOnBackgroundVariant,
    onBackgroundVariantTwo = primaryDarkOnBackgroundVariantTwo,
    bottomNav = primaryDarkBottomNav,
    transparent = transparent,
    primaryVariantThree = primaryDarkVariantThree,
    secondaryVariantThree = secondaryDarkVariantThree,
    accentVariantTwo = accentDarkVariantTwo,
    accentVariantThree = accentDarkVariantTwo,
    accentTwo = accentTwoDark,
    accentThree = accentThreeDark,
    accentThreeVariant = accentThreeDarkVariant)

data class SizzleColors(
    val primary: Color,
    val primaryVariant: Color,
    val primaryVariantTwo: Color,
    val primaryVariantThree: Color,
    val secondary: Color,
    val secondaryVariant: Color,
    val secondaryVariantTwo: Color,
    val secondaryVariantThree: Color,
    val surface: Color,
    val background: Color,
    val error: Color,
    val accent: Color,
    val accentVariant: Color,
    val accentVariantTwo: Color,
    val accentVariantThree: Color,
    val accentTwo: Color,
    val accentThree: Color,
    val accentThreeVariant: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onSurface: Color,
    val onBackground: Color,
    val onError: Color,
    val onBackgroundSurface: Color,
    val onBackgroundVariant: Color,
    val onBackgroundVariantTwo: Color,
    val bottomNav: Color,
    val transparent: Color
)

val defaultColors = sizzleLightColors

@Composable
fun sizzleColors(darkTheme: Boolean): SizzleColors {
    return when (darkTheme) {
        true -> sizzleDarkColors
        false -> sizzleLightColors
    }
}