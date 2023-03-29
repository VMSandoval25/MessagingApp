package app.getsizzle.messaging.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.getsizzle.messaging.R.font


@Stable
val sarabunFontFamily = FontFamily(
    Font(resId = font.sarabun_regular, weight = FontWeight.Normal),
    Font(resId = font.sarabun_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = font.sarabun_medium, weight = FontWeight.Medium),
    Font(resId = font.sarabun_semibold, weight = FontWeight.SemiBold),
    Font(resId = font.sarabun_bold, weight = FontWeight.Bold),
    Font(resId = font.sarabun_extrabold, weight = FontWeight.ExtraBold)
)

private val displayLarge = TextStyle(
    fontSize = 57.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    lineHeight = 64.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val displayMedium = TextStyle(
    fontSize = 45.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    lineHeight = 52.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val displaySmall = TextStyle(
    fontSize = 36.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    lineHeight = 44.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val headlineLarge = TextStyle(
    fontSize = 32.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 40.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val headlineMedium = TextStyle(
    fontSize = 28.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 36.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val headlineSmall = TextStyle(
    fontSize = 24.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 32.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val titleLarge = TextStyle(
    fontSize = 22.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 28.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val titleMedium = TextStyle(
    fontSize = 16.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = 0.15.sp,
    lineHeight = 20.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val titleSmall = TextStyle(
    fontSize = 14.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = 0.1.sp,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val bodyLarge = TextStyle(
    fontSize = 16.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val bodyMedium = TextStyle(
    fontSize = 14.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val bodySmall = TextStyle(
    fontSize = 12.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.4.sp,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val labelLarge = TextStyle(
    fontSize = 14.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.5.sp,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val labelMedium = TextStyle(
    fontSize = 12.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.5.sp,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val labelSmall = TextStyle(
    fontSize = 10.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)
private val button = TextStyle(
    fontSize = 12.sp,
    fontFamily = sarabunFontFamily,
    fontWeight = FontWeight.Bold,
    lineHeight = 16.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
)

@Immutable
data class SizzleTypography(
    val defaultFontFamily: FontFamily,
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelSmall: TextStyle,
    val labelMedium: TextStyle,
    val labelLarge: TextStyle,
    val button: TextStyle
)

val defaultTypography = sizzleTypography()

fun sizzleTypography(): SizzleTypography {
    return SizzleTypography(
        defaultFontFamily = sarabunFontFamily,
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
        labelSmall = labelSmall,
        labelMedium = labelMedium,
        labelLarge = labelLarge,
        button = button
    )
}