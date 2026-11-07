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
            fontSize = if (isArabic) 40.sp else 36.sp,
            lineHeight = if (isArabic) 50.sp else 44.sp
        ),
        displayMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 34.sp else 30.sp,
            lineHeight = if (isArabic) 44.sp else 38.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 29.sp else 26.sp,
            lineHeight = if (isArabic) 38.sp else 34.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 25.sp else 22.sp,
            lineHeight = if (isArabic) 34.sp else 30.sp
        ),
        titleLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 23.sp else 20.sp,
            lineHeight = if (isArabic) 32.sp else 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 20.sp else 18.sp,
            lineHeight = if (isArabic) 28.sp else 25.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 19.sp else 17.sp,
            lineHeight = if (isArabic) 28.sp else 25.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 17.sp else 15.sp,
            lineHeight = if (isArabic) 25.sp else 22.sp
        ),
        labelLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Bold,
            fontSize = if (isArabic) 18.sp else 16.sp,
            lineHeight = if (isArabic) 25.sp else 22.sp
        )
    )
}

val Typography = getAppTypography(isArabic = true)
