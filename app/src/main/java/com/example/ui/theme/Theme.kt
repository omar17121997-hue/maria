package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val FunLearningColorScheme = lightColorScheme(
    primary = MagicPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE7F6),
    onPrimaryContainer = DeepNavyText,
    secondary = LuluPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEBF0),
    onSecondaryContainer = DeepNavyText,
    tertiary = SunnyYellow,
    onTertiary = DeepNavyText,
    tertiaryContainer = Color(0xFFFFF8E1),
    onTertiaryContainer = DeepNavyText,
    background = SkyBlueTop,
    onBackground = DeepNavyText,
    surface = Color.White,
    onSurface = DeepNavyText,
    surfaceVariant = Color(0xFFF4F7FB),
    onSurfaceVariant = SoftSlateText
)

val FunShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp)
)

@Composable
fun FunLearningWorldTheme(
    isArabic: Boolean = true,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        MaterialTheme(
            colorScheme = FunLearningColorScheme,
            typography = getAppTypography(isArabic = isArabic),
            shapes = FunShapes,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    FunLearningWorldTheme(isArabic = true, content = content)
}
