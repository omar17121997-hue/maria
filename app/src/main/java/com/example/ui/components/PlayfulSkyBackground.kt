package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.PastelMeadowBottom
import com.example.ui.theme.SkyBlueMiddle
import com.example.ui.theme.SkyBlueTop
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renders a dreamy, cheerful children's storybook background featuring:
 * - Soft pastel sky-to-meadow gradient
 * - Pastel rainbow arc in the top sky
 * - Gently drifting 3D fluffy clouds
 * - Twinkling golden stars
 * - Soft rolling meadow hills with tiny pastel flowers at the bottom
 */
@Composable
fun PlayfulSkyBackground(
    modifier: Modifier = Modifier,
    accentTint: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sky_decorations")

    val cloudDrift by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud_drift"
    )

    val starTwinkle by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_twinkle"
    )

    val topColor = if (accentTint != null) {
        Color(
            red = (SkyBlueTop.red * 0.78f + accentTint.red * 0.22f),
            green = (SkyBlueTop.green * 0.78f + accentTint.green * 0.22f),
            blue = (SkyBlueTop.blue * 0.78f + accentTint.blue * 0.22f),
            alpha = 1f
        )
    } else {
        SkyBlueTop
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topColor,
                        SkyBlueMiddle,
                        PastelMeadowBottom
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Soft Pastel Rainbow Arc across top sky
            val rainbowColors = listOf(
                Color(0x38FF6F91),
                Color(0x38FFB74D),
                Color(0x38FFF176),
                Color(0x3881C784),
                Color(0x384FC3F7),
                Color(0x38BA68C8)
            )
            val arcBaseWidth = w * 0.95f
            val arcLeft = (w - arcBaseWidth) / 2f
            rainbowColors.forEachIndexed { idx, color ->
                val inset = idx * 14f
                drawArc(
                    color = color,
                    startAngle = 190f,
                    sweepAngle = 160f,
                    useCenter = false,
                    topLeft = Offset(arcLeft + inset, h * 0.04f + inset),
                    size = Size(arcBaseWidth - inset * 2, w * 0.65f - inset * 2),
                    style = Stroke(width = 13f, cap = StrokeCap.Round)
                )
            }

            // 2. Drifting Fluffy Clouds
            drawFluffyCloud(
                centerX = w * 0.16f + cloudDrift,
                centerY = h * 0.11f,
                scale = 1.0f
            )
            drawFluffyCloud(
                centerX = w * 0.84f - cloudDrift * 0.8f,
                centerY = h * 0.15f,
                scale = 0.88f
            )
            drawFluffyCloud(
                centerX = w * 0.12f - cloudDrift * 0.6f,
                centerY = h * 0.48f,
                scale = 0.72f
            )
            drawFluffyCloud(
                centerX = w * 0.88f + cloudDrift * 0.7f,
                centerY = h * 0.56f,
                scale = 0.75f
            )

            // 3. Twinkling Golden & Pastel Stars
            val starPositions = listOf(
                Triple(w * 0.08f, h * 0.06f, 14f),
                Triple(w * 0.92f, h * 0.08f, 16f),
                Triple(w * 0.50f, h * 0.04f, 12f),
                Triple(w * 0.06f, h * 0.29f, 13f),
                Triple(w * 0.94f, h * 0.33f, 15f),
                Triple(w * 0.22f, h * 0.74f, 12f),
                Triple(w * 0.80f, h * 0.78f, 14f)
            )
            starPositions.forEachIndexed { i, (sx, sy, radius) ->
                val alpha = if (i % 2 == 0) starTwinkle else (1.35f - starTwinkle).coerceIn(0.4f, 1f)
                drawFourPointSparkle(
                    center = Offset(sx, sy),
                    radius = radius,
                    color = Color(0xFFFFCA28).copy(alpha = alpha * 0.75f)
                )
            }

            // 4. Gentle Rolling Pastel Hills at the bottom
            val hillPathLeft = Path().apply {
                moveTo(0f, h)
                lineTo(0f, h * 0.90f)
                quadraticTo(w * 0.28f, h * 0.85f, w * 0.62f, h * 0.93f)
                quadraticTo(w * 0.84f, h * 0.97f, w, h * 0.91f)
                lineTo(w, h)
                close()
            }
            drawPath(
                path = hillPathLeft,
                color = Color(0xFFE8F8E8).copy(alpha = 0.75f)
            )

            // 5. Cute Pastel Meadow Flowers along the bottom hill
            drawCuteFlower(Offset(w * 0.09f, h * 0.91f), petalColor = Color(0xFFFF80AB), radius = 9f)
            drawCuteFlower(Offset(w * 0.24f, h * 0.89f), petalColor = Color(0xFF80D8FF), radius = 8f)
            drawCuteFlower(Offset(w * 0.78f, h * 0.93f), petalColor = Color(0xFFFFB74D), radius = 9f)
            drawCuteFlower(Offset(w * 0.91f, h * 0.91f), petalColor = Color(0xFFCE93D8), radius = 8f)
        }

        content()
    }
}

private fun DrawScope.drawFluffyCloud(centerX: Float, centerY: Float, scale: Float) {
    val r = 28f * scale
    // Soft bottom shadow for 3D puffiness
    val shadowColor = Color(0xFFD0ECF9).copy(alpha = 0.55f)
    drawCircle(shadowColor, radius = r * 1.1f, center = Offset(centerX, centerY + 6f * scale))
    drawCircle(shadowColor, radius = r * 0.85f, center = Offset(centerX - r * 0.9f, centerY + 8f * scale))
    drawCircle(shadowColor, radius = r * 0.85f, center = Offset(centerX + r * 0.9f, centerY + 8f * scale))

    // Main white cloud puffs
    val cloudColor = Color.White.copy(alpha = 0.88f)
    drawCircle(cloudColor, radius = r * 1.15f, center = Offset(centerX, centerY))
    drawCircle(cloudColor, radius = r * 0.9f, center = Offset(centerX - r * 0.9f, centerY + 4f * scale))
    drawCircle(cloudColor, radius = r * 0.9f, center = Offset(centerX + r * 0.9f, centerY + 4f * scale))
}

private fun DrawScope.drawFourPointSparkle(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        quadraticTo(center.x, center.y, center.x + radius, center.y)
        quadraticTo(center.x, center.y, center.x, center.y + radius)
        quadraticTo(center.x, center.y, center.x - radius, center.y)
        quadraticTo(center.x, center.y, center.x, center.y - radius)
        close()
    }
    drawPath(path = path, color = color)
    drawCircle(color = Color.White.copy(alpha = 0.85f), radius = radius * 0.22f, center = center)
}

private fun DrawScope.drawCuteFlower(center: Offset, petalColor: Color, radius: Float) {
    for (angle in 0 until 360 step 72) {
        val rad = Math.toRadians(angle.toDouble())
        val px = center.x + (cos(rad) * radius).toFloat()
        val py = center.y + (sin(rad) * radius).toFloat()
        drawCircle(color = petalColor.copy(alpha = 0.85f), radius = radius * 0.65f, center = Offset(px, py))
    }
    drawCircle(color = Color(0xFFFFEE58), radius = radius * 0.5f, center = center)
}
