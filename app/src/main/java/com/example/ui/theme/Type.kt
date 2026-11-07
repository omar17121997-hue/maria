package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val CairoFontFamily = FontFamily(
    Font(R.font.cairo, FontWeight.Normal),
    Font(R.font.cairo, FontWeight.Medium),
    Font(R.font.cairo, FontWeight.Bold)
)

val FredokaFontFamily = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal),
    Font(R.font.fredoka, FontWeight.Medium),
    Font(R.font.fredoka, FontWeight.Bold)
)

fun getAppTypography(isArabic: Boolean): Typography {
    val primaryFont = if (isArabic) CairoFontFamily else FredokaFontFamily
    return Typography(
        displayLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
        displayMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            lineHeight = 38.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 34.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 30.sp
        ),
        titleLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 25.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 25.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 22.sp
        ),
        labelLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    )
}

val Typography = getAppTypography(isArabic = true)
